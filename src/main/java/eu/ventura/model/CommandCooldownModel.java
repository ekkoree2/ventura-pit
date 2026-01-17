package eu.ventura.model;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * author: ekkoree
 * created at: 1/15/2025
 */
@Getter
public class CommandCooldownModel {
    private final Map<String, Map<UUID, Long>> cooldowns = new HashMap<>();
    private static CommandCooldownModel instance;

    public static CommandCooldownModel getInstance() {
        if (instance == null) {
            instance = new CommandCooldownModel();
        }
        return instance;
    }

    public void setCooldown(String key, Player player, int seconds) {
        cooldowns.computeIfAbsent(key, k -> new HashMap<>())
                .put(player.getUniqueId(), System.currentTimeMillis() + (seconds * 1000L));
    }

    public boolean isOnCooldown(String key, Player player) {
        Map<UUID, Long> cooldownMap = cooldowns.get(key);
        if (cooldownMap == null) {
            return false;
        }

        Long cooldownTime = cooldownMap.get(player.getUniqueId());
        if (cooldownTime == null) {
            return false;
        }

        return System.currentTimeMillis() < cooldownTime;
    }
}
