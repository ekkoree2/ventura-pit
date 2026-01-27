package eu.ventura.listener;

import eu.ventura.Pit;
import eu.ventura.constants.NumberFormat;
import eu.ventura.constants.Strings;
import eu.ventura.java.NewString;
import eu.ventura.model.PlayerModel;
import eu.ventura.model.ScoreboardModel;
import eu.ventura.service.PlayerService;
import eu.ventura.util.LevelUtil;
import eu.ventura.util.MathUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * author: ekkoree
 * created at: 1/15/2025
 */
public class ScoreboardListener implements Listener {
    private final Map<UUID, ScoreboardModel> scoreboardModels = new ConcurrentHashMap<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");

    public ScoreboardListener() {
        new BukkitRunnable() {
            @Override
            public void run() {
                updateAllScoreboards();
            }
        }.runTaskTimer(Pit.instance, 0L, 1L);
    }

    private void updateAllScoreboards() {
        String currentDate = dateFormat.format(new Date());

        scoreboardModels.forEach((uuid, scoreboardModel) -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                updateScoreboard(player, scoreboardModel, currentDate);
            }
        });
    }

    private void updateScoreboard(Player player, ScoreboardModel scoreboardModel, String currentDate) {
        PlayerModel model = PlayerService.getPlayer(player);

        List<String> newLines = new ArrayList<>();

        newLines.add("§7" + currentDate);
        newLines.add("");

        if (Pit.event != null) {
            newLines.addAll(Pit.event.getScoreboard(player));
            newLines.add("");
        }

        if (model.getPrestige() > 0) {
            newLines.add(Strings.Formatted.PRESTIGE.format(player, MathUtil.roman(model.getPrestige())));
        }

        NumberFormat.Formatter format = NumberFormat.GOLD_DISPLAY_LOW;
        if (model.getGold() > 10000.0) {
            format = NumberFormat.GOLD_DISPLAY_HIGH;
        }
        newLines.add(Strings.Formatted.LEVEL.format(player, LevelUtil.getFormattedLevelFromValuesChat(model)));
        newLines.add(model.getLevel() == 120
                ? Strings.Simple.MAXED.get(player)
                : Strings.Formatted.NEEDED_XP.format(player, NumberFormat.DEF.of(model.requiredXP)));
        newLines.add("");
        newLines.add(Strings.Formatted.GOLD.format(player, format.of(model.getGold())));
        newLines.add("");
        String status = model.status.getTitle(model);
        if (model.combatTime <= 5 && model.status == eu.ventura.constants.Status.FIGHTING) {
            status += String.format("§7 (%d)", model.combatTime);
        }
        if (Pit.event != null && model.getPrestige() > 0) {
            status = "§6Event";
        }
        newLines.add(Strings.Formatted.STATUS.format(player, status));
        if (model.bounty > 0 && Pit.event == null) {
            newLines.add(Strings.Formatted.BOUNTY_SCOREBOARD.format(player, NumberFormat.DEF.of(model.bounty)));
        }
        if (model.streak > 0 && Pit.event == null) {
            newLines.add(Strings.Formatted.STREAK.format(player, NumberFormat.DEF.of(model.streak)));
        }
        newLines.add("");
        newLines.add(NewString.of("&ehvh.venturaclient.eu"));

        scoreboardModel.updateLines(newLines);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ScoreboardModel scoreboardModel = new ScoreboardModel(player);
        scoreboardModels.put(player.getUniqueId(), scoreboardModel);
        updateScoreboard(player, scoreboardModel, dateFormat.format(new Date()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        scoreboardModels.remove(player.getUniqueId());
    }
}
