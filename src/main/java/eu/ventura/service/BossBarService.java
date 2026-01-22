package eu.ventura.service;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * author: ekkoree
 * created at: 1/22/2026
 */
public class BossBarService {
    private static BossBarService instance;
    private final Map<UUID, BossBar> bars = new HashMap<>();
    private final Map<UUID, Float> maxHealths = new HashMap<>();

    public static BossBarService getInstance() {
        if (instance == null) {
            instance = new BossBarService();
        }
        return instance;
    }

    public void start() {}

    public void stop() {
        for (BossBar bar : bars.values()) {
            bar.removeAll();
        }
        bars.clear();
        maxHealths.clear();
    }

    public void create(Player player, String text, float health) {
        UUID uuid = player.getUniqueId();
        remove(player);

        BossBar bar = Bukkit.createBossBar(text, BarColor.PURPLE, BarStyle.SOLID);
        bar.setProgress(1.0);
        bar.addPlayer(player);

        bars.put(uuid, bar);
        maxHealths.put(uuid, health);
    }

    public void updateHealth(Player player, String text, float health) {
        UUID uuid = player.getUniqueId();
        BossBar bar = bars.get(uuid);
        if (bar == null) return;

        float maxHealth = maxHealths.getOrDefault(uuid, 300f);
        double progress = Math.max(0.0, Math.min(1.0, health / maxHealth));

        bar.setTitle(text);
        bar.setProgress(progress);
    }

    public void remove(Player player) {
        UUID uuid = player.getUniqueId();
        BossBar bar = bars.get(uuid);
        if (bar != null) {
            bar.removeAll();
            bars.remove(uuid);
        }
    }

    public void cleanup(Player player) {
        remove(player);
        maxHealths.remove(player.getUniqueId());
    }
}
