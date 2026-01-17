package eu.ventura.model;

import eu.ventura.constants.EffectSource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * author: ekkoree
 * created at: 1/14/2026
 */
@Getter
@RequiredArgsConstructor
public class PlayerEffectsModel {
    private final Map<String, Integer> comboEnchants = new HashMap<>();
    private final Map<String, Long> enchantsCooldown = new HashMap<>();
    private final Map<EffectSource, Map<String, Long>> activeEffects = new HashMap<>();
    private final Map<String, Boolean> permanentEffects = new HashMap<>();
    private final Map<String, BukkitTask> activeTasks = new HashMap<>();
    private final Map<String, Long> comboEnchantsCd = new HashMap<>();
    private final Map<String, UUID> effectAttackers = new HashMap<>();
    private final PlayerModel playerModel;

    public void setActive(EffectSource source, String id, int seconds) {
        activeEffects
                .computeIfAbsent(source, s -> new HashMap<>())
                .put(id, System.currentTimeMillis() + seconds * 1000L);
    }

    public void setActive(EffectSource source, String id, int seconds, UUID attacker) {
        setActive(source, id, seconds);
        if (attacker != null) {
            effectAttackers.put(id, attacker);
        }
    }

    public UUID getAttacker(String id) {
        return effectAttackers.get(id);
    }

    public int getRemaining(EffectSource source, String id) {
        Map<String, Long> sourceMap = activeEffects.get(source);
        if (sourceMap == null) return 0;

        Long expiration = sourceMap.get(id);
        if (expiration == null) return 0;

        long remaining = expiration - System.currentTimeMillis();
        if (remaining <= 0) {
            sourceMap.remove(id);
            return 0;
        }

        return (int) (remaining / 1000);
    }

    public static PlayerEffectsModel get(Player player) {
        return PlayerModel.getInstance(player).getEffectsModel();
    }

    public boolean isActive(EffectSource source, String id) {
        Map<String, Long> sourceMap = activeEffects.get(source);
        if (sourceMap == null) {
            return false;
        }
        Long expiration = sourceMap.get(id);
        if (expiration == null) {
            return false;
        }
        if (System.currentTimeMillis() >= expiration) {
            sourceMap.remove(id);
            return false;
        }
        return true;
    }

    public void reduceCooldown(String id, Long value) {
        long cd = enchantsCooldown.getOrDefault(id, 0L);
        cd = Math.min(0, cd - value);
        enchantsCooldown.put(id, cd);
    }

    public void setCombo(String id, int value) {
        comboEnchants.put(id, value);
    }

    public int getCombo(String id) {
        return comboEnchants.getOrDefault(id, 0);
    }

    public boolean canCombo(String id, int levelTrigger) {
        if (playerModel.player.hasMetadata("regularity-strike") ||
                playerModel.player.hasMetadata("volley-strike")) {
            return false;
        }

        if (isActive(EffectSource.ENCHANT, "combo-venom")) {
            return false;
        }

        if (comboEnchants.getOrDefault(id, 0) >= levelTrigger - 1) {
            comboEnchants.put(id, 0);
            return true;
        }
        comboEnchants.put(id, comboEnchants.getOrDefault(id, 0) + 1);
        return false;
    }

    public boolean canCombo(String id, int level, long cd) {
        if (playerModel.player.hasMetadata("regularity-strike") ||
                playerModel.player.hasMetadata("volley-strike")) {
            return false;
        }

        if (isActive(EffectSource.ENCHANT, "combo-venom")) {
            return false;
        }

        long now = System.currentTimeMillis();
        long last = comboEnchantsCd.getOrDefault(id, 0L);

        if (now - last > cd) {
            comboEnchants.put(id, 0);
        }

        comboEnchantsCd.put(id, now);

        int current = comboEnchants.getOrDefault(id, 0);
        if (current >= level - 1) {
            comboEnchants.put(id, 0);
            return true;
        }

        comboEnchants.put(id, current + 1);
        return false;
    }

    public boolean canCooldown(String id, long cooldown) {
        if (System.currentTimeMillis() - enchantsCooldown.getOrDefault(id, 0L) >= cooldown) {
            enchantsCooldown.put(id, System.currentTimeMillis());
            return true;
        }
        return false;
    }

    public void setCooldown(String id, long delay) {
        enchantsCooldown.put(id, delay);
    }

    public boolean isPermanent(String id) {
        return permanentEffects.getOrDefault(id, false);
    }

    public void setPermanent(String id, Boolean value) {
        permanentEffects.put(id, value);
    }

    public void clearEnchant(String id) {
        for (Map<String, Long> effectMap : activeEffects.values()) {
            effectMap.entrySet().removeIf(entry -> entry.getKey().startsWith(id));
        }

        activeTasks.entrySet().removeIf(entry -> {
            if (entry.getKey().startsWith(id)) {
                BukkitTask task = entry.getValue();
                if (task != null) task.cancel();
                return true;
            }
            return false;
        });

        comboEnchants.entrySet().removeIf(entry -> entry.getKey().startsWith(id));
        enchantsCooldown.entrySet().removeIf(entry -> entry.getKey().startsWith(id));
        comboEnchantsCd.entrySet().removeIf(entry -> entry.getKey().startsWith(id));
        permanentEffects.entrySet().removeIf(entry -> entry.getKey().startsWith(id));
        effectAttackers.entrySet().removeIf(entry -> entry.getKey().startsWith(id));
    }

    public void clear() {
        for (Map.Entry<EffectSource, Map<String, Long>> entry : activeEffects.entrySet()) {
            if (entry.getKey() != EffectSource.CUSTOM) {
                entry.getValue().entrySet().removeIf(effect -> !isPermanent(effect.getKey()));
            }
        }
        enchantsCooldown.clear();
        comboEnchants.clear();

        for (Map.Entry<String, BukkitTask> entry : activeTasks.entrySet()) {
            if (!isPermanent(entry.getKey())) {
                BukkitTask task = entry.getValue();
                if (task != null) task.cancel();
            }
        }
        activeTasks.entrySet().removeIf(entry -> !isPermanent(entry.getKey()));
        comboEnchantsCd.clear();
        effectAttackers.entrySet().removeIf(entry -> !isPermanent(entry.getKey()));
    }
}