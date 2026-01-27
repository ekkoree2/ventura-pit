package eu.ventura.events.major;

import eu.ventura.Pit;
import eu.ventura.constants.Sounds;
import eu.ventura.constants.Strings;
import eu.ventura.event.PitDamageEvent;
import eu.ventura.event.PitKillEvent;
import eu.ventura.model.PlayerModel;
import eu.ventura.service.BossBarService;
import eu.ventura.service.MapService;
import eu.ventura.util.NBTTag;
import eu.ventura.util.PlayerUtil;
import eu.ventura.util.RegionHelper;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * author: ekkoree
 * created at: 1/22/2026
 */
@Getter
public abstract class MajorEvent {
    private final Set<UUID> players = new HashSet<>();
    private BukkitTask task;
    private BukkitTask runnable;
    private int time;
    private int phase;
    private int ticks;
    private int seconds;

    public abstract String getEventId();

    public abstract String getDisplayName();

    public abstract List<String> getScoreboard(Player player);

    protected abstract int getDuration();

    protected abstract List<String> getEndingMessage(Player player);

    protected String getBossBar(Player player, int phase) {
        return null;
    }

    protected String formatTime() {
        return String.format("%02d:%02d", time / 60, time % 60);
    }

    protected String animWave(String text, String h1, String h2, String base, int phase) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            int d = Math.abs(i - phase);
            sb.append(d == 0 ? h1 : d == 1 ? h2 : base).append(text.charAt(i));
        }
        return sb.toString();
    }

    public void onQuit(PlayerQuitEvent e) {
        players.remove(e.getPlayer().getUniqueId());
        BossBarService.getInstance().remove(e.getPlayer());
    }

    public void onJoin(PlayerJoinEvent e) {
        players.add(e.getPlayer().getUniqueId());
        showBar(e.getPlayer());
    }

    public void onDamage(PitDamageEvent e) {}

    public void onKill(PitKillEvent e) {}

    protected void onEnable() {}

    protected void onDisable() {}

    public void onInteract(PlayerInteractEvent event) {}

    private void showBar(Player p) {
        String text = getBossBar(p, phase);
        if (text != null) BossBarService.getInstance().create(p, text, time);
    }

    private void updateBar(Player p) {
        String text = getBossBar(p, phase);
        if (text != null) BossBarService.getInstance().updateHealth(p, text, time);
    }

    protected String getTimerBossy(Player player, int phase) {
        int c = phase % 88;
        String m = "§5§lMAJOR EVENT!";
        String r = " " + getDisplayName();

        if (c < 12) {
            m = animWave("MAJOR EVENT!", "§d§l", "§f§l", "§5§l", c);
        } else if (c >= 16 && c < 28) {
            m = animWave("MAJOR EVENT!", "§d§l", "§f§l", "§5§l", c - 16);
        } else if (c >= 48 && c < 68) {
            r = (c - 48) % 10 < 5 ? " " + getDisplayName().replace("§c", "§d") : " " + getDisplayName() + "!";
        }

        String timer = String.format("%02d:%02d", seconds / 60, seconds % 60);
        String starting = Strings.Formatted.MAJOR_EVENT_BOSSBAR_STARTING.format(player, timer);
        return m + r + " " + starting;
    }

    private String getChatMessage(Player player, int minutes) {
        String suffix = minutes == 1 ? "" : "s";
        return Strings.Formatted.MAJOR_EVENT_CHAT.format(player, getDisplayName(), minutes, suffix);
    }

    public void schedule() {
        seconds = 180;

        if (Bukkit.getOnlinePlayers().size() < 2) {
            return;
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            BossBarService.getInstance().create(p, getTimerBossy(p, 0), 180);
            p.sendMessage(getChatMessage(p, 3));
            Sounds.MAJOR_EVENT_SCHEDULE.play(p);
        }

        int[] timer = {0};
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                timer[0]++;

                for (Player p : Bukkit.getOnlinePlayers()) {
                    BossBarService.getInstance().updateHealth(p, getTimerBossy(p, timer[0]), seconds);
                }

                if (timer[0] % 20 == 0) {
                    seconds--;

                    if (seconds <= 0) {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            BossBarService.getInstance().remove(p);
                        }
                        start();
                        cancel();
                        return;
                    }

                    if (seconds % 60 == 0) {
                        int minutes = seconds / 60;
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.sendMessage(getChatMessage(p, minutes));
                        }
                    }
                }
            }
        }.runTaskTimer(Pit.instance, 0L, 1L);
    }

    public void start() {
        if (Pit.event != null) {
            return;
        }

        Pit.event = this;
        time = getDuration();
        phase = 0;
        ticks = 0;

        if (Bukkit.getOnlinePlayers().size() < 2) {
            return;
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            players.add(p.getUniqueId());

            if (!RegionHelper.isInSpawn(p)) {
                Location location = MapService.getRandomSpawnLocation().of();
                p.teleport(location);
            }

            Bukkit.getScheduler().runTask(Pit.instance, () -> {
                showBar(p);
                Sounds.MAJOR_START.play(p);
                p.sendTitle("§6§lPIT EVENT!", getDisplayName(), 10, 100, 10);
                PlayerUtil.updateMaxHealth(p, true);
            });
        }

        task = Bukkit.getScheduler().runTaskTimer(Pit.instance, () -> {
            ticks++;
            phase++;

            for (UUID uuid : players) {
                Player p = Bukkit.getPlayer(uuid);
                if (p != null) {
                    updateBar(p);
                }
            }

            if (ticks % 20 == 0) {
                time--;
                if (time <= 0) {
                    stop();
                }
            }
        }, 0, 1L);

        onEnable();
    }

    private String formatTime(int seconds) {
        return String.format("%02d:%02d", seconds / 60, seconds % 60);
    }

    public void stop() {
        if (runnable != null) {
            runnable.cancel();
            runnable = null;
        }

        if (task != null) {
            task.cancel();
            task = null;
        }

        Pit.event = null;

        for (UUID uuid : players) {
            Player p = Bukkit.getPlayer(uuid);
            if (p == null) continue;

            BossBarService.getInstance().remove(p);
            Sounds.MAJOR_END.play(p);

            PlayerUtil.updateMaxHealth(p, false);
            PlayerModel.getInstance(p).clearItemsByTag(NBTTag.EVENT_ITEM);

            List<String> msg = getEndingMessage(p);
            if (msg != null) msg.forEach(p::sendMessage);
        }

        onDisable();
        players.clear();
    }
}
