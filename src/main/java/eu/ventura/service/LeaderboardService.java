package eu.ventura.service;

import com.mongodb.client.MongoCollection;
import eu.ventura.Pit;
import eu.ventura.constants.NumberFormat;
import eu.ventura.constants.Strings;
import eu.ventura.model.PlayerModel;
import eu.ventura.util.LevelUtil;
import eu.ventura.util.MongoUtil;
import eu.ventura.util.PlayerUtil;
import hvh.ventura.hologram.Hologram;
import hvh.ventura.hologram.api.HologramApi;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * author: ekkoree
 * created at: 1/22/2026
 */
public class LeaderboardService {
    private static LeaderboardService instance;
    private final HologramApi hologramApi;
    private final ConcurrentHashMap<UUID, Hologram> holograms = new ConcurrentHashMap<>();
    private final Location leaderboardLocation;
    private BukkitTask updateTask;

    public static LeaderboardService getInstance(HologramApi hologramApi, Location location) {
        if (instance == null) {
            instance = new LeaderboardService(hologramApi, location);
        }
        return instance;
    }

    private LeaderboardService(HologramApi hologramApi, Location location) {
        this.hologramApi = hologramApi;
        this.leaderboardLocation = location;
    }

    public void start() {
        updateTask = Bukkit.getScheduler().runTaskTimer(Pit.instance, this::updateAllHolograms, 1L, 100L);
    }

    public void stop() {
        if (updateTask != null) {
            updateTask.cancel();
        }
        for (Hologram hologram : holograms.values()) {
            hologramApi.destroy(hologram);
        }
        holograms.clear();
    }

    private void updateAllHolograms() {
        for (UUID uuid : new ArrayList<>(holograms.keySet())) {
            Hologram hologram = holograms.get(uuid);
            if (hologram != null) {
                Player player = Bukkit.getPlayer(uuid);
                if (player != null && player.isOnline()) {
                    showHologram(player);
                }
            }
        }
    }

    public void showHologram(Player player) {
        List<String> lines = getLeaderboardLines(player);
        updateHologram(player, lines);
    }

    public void removeHologram(Player player) {
        Hologram hologram = holograms.remove(player.getUniqueId());
        if (hologram != null) {
            Bukkit.getScheduler().runTask(Pit.instance, () -> hologramApi.destroy(hologram));
        }
    }

    private void updateHologram(Player player, List<String> lines) {
        if (!player.isOnline()) return;

        UUID uuid = player.getUniqueId();
        Hologram existing = holograms.get(uuid);

        if (existing == null) {
            String[] linesArray = lines.toArray(new String[0]);
            Hologram created = hologramApi.createHologram(leaderboardLocation, uuid, linesArray);
            created.spawn();
            holograms.put(uuid, created);
            created.show(player);
        } else {
            for (int i = 0; i < lines.size(); i++) {
                existing.updateLine(i, lines.get(i));
            }
        }
    }

    private List<String> getLeaderboardLines(Player player) {
        PlayerModel model = PlayerModel.getInstance(player);
        List<String> lines = new ArrayList<>();
        lines.add(Strings.Simple.LEADERBOARD_HEADER.get(model.language));
        lines.add(Strings.Simple.LEADERBOARD_SUBTITLE.get(model.language));
        lines.add("");

        List<PlayerStats> topPlayers = getTopPlayers();
        int position = 1;

        for (PlayerStats stats : topPlayers) {
            String level = LevelUtil.getFormattedLevelFromValues(stats.prestige, stats.level);
            String rankColor = stats.rankColor != null ? stats.rankColor.replace("&", "§") : "§7";
            String formattedXP = NumberFormat.formatLarge(stats.score);

            lines.add(Strings.Formatted.LEADERBOARD_ENTRY.format(model.language, position, level, rankColor + stats.username, formattedXP));
            position++;
        }

        lines.add("");
        String playerXP = NumberFormat.formatLarge(calculateScore(model.getPrestige(), model.getLevel()));
        lines.add(Strings.Formatted.LEADERBOARD_SUFFIX.format(player, playerXP));
        return lines;
    }

    private List<PlayerStats> getTopPlayers() {
        List<PlayerStats> result = new ArrayList<>();
        MongoCollection<Document> collection = MongoUtil.getCollection("players");
        for (Document doc : collection.find()) {
            try {
                String username = doc.getString("username");
                if (username == null) continue;

                String uuidString = doc.getString("_id");
                if (uuidString == null) continue;
                UUID uuid = UUID.fromString(uuidString);

                int level;
                int prestige;
                Player onlinePlayer = Bukkit.getPlayer(uuid);
                if (onlinePlayer != null && onlinePlayer.isOnline()) {
                    PlayerModel model = PlayerModel.getInstance(onlinePlayer);
                    level = model.getLevel();
                    prestige = model.getPrestige();
                } else {
                    level = doc.getInteger("level", 1);
                    prestige = doc.getInteger("prestige", 0);
                }

                String rankColor = PlayerUtil.getRankColor(uuid);

                long score = calculateScore(prestige, level);
                result.add(new PlayerStats(username, level, prestige, rankColor, score));
            } catch (Exception e) {
                System.out.println("[Leaderboard] Error processing document: " + e.getMessage());
            }
        }

        result.sort((a, b) -> Long.compare(b.score, a.score));
        return result.stream().limit(10).toList();
    }

    private long calculateScore(int prestige, int level) {
        long totalXp = 0;
        for (int p = 0; p < prestige; p++) {
            for (int l = 1; l <= 119; l++) {
                totalXp += LevelUtil.xpToNextLevel(p, l);
            }
        }
        for (int l = 1; l < level; l++) {
            totalXp += LevelUtil.xpToNextLevel(prestige, l);
        }
        return totalXp;
    }

    private record PlayerStats(String username, int level, int prestige, String rankColor, long score) {}
}
