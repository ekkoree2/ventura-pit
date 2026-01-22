package eu.ventura.events.major;

import eu.ventura.Pit;
import eu.ventura.constants.Sounds;
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
    private int time;
    private int phase;
    private int ticks;

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

    public void start() {
        Pit.event = this;
        time = getDuration();
        phase = 0;
        ticks = 0;



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
                if (p != null) updateBar(p);
            }

            if (ticks >= 20) {
                ticks = 0;
                time--;
                if (time <= 0) stop();
            }
        }, 1L, 1L);

        onEnable();
    }

    public void stop() {
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
