package eu.ventura.util;

import eu.ventura.Pit;
import eu.ventura.constants.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * author: ekkoree
 * created at: 1/22/2026
 */
public class ConsumableUtil {
    private static final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();

    public static boolean tryConsume(Player player, ItemStack item, String consumableId, int cooldownMs, Consumer<Player> effectApplier) {
        UUID uuid = player.getUniqueId();
        Map<String, Long> playerCooldowns = cooldowns.computeIfAbsent(uuid, k -> new HashMap<>());

        long lastUse = playerCooldowns.getOrDefault(consumableId, 0L);
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastUse < cooldownMs) {
            Sounds.GOLDEN_HEADS_COOLDOWN.play(player);
            return false;
        }

        Sounds.GOLDEN_HEADS.play(player);
        ItemStack decreased = ItemHelper.decreaseItemAmount(item);

        playerCooldowns.put(consumableId, currentTime);
        player.getInventory().setItemInMainHand(decreased);

        effectApplier.accept(player);
        return true;
    }

    public static void applyGoldenHeadsEffects(Player player) {
        double abs = player.getAbsorptionAmount();
        double newAbs = Math.min(abs + 4, 6);

        if (newAbs > abs) {
            Bukkit.getScheduler().runTask(Pit.instance, () -> {
                PlayerUtil.setAbs(player, newAbs);
            });
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 160, 0));
    }

    public static void applyRagePotatoEffects(Player player) {
        double abs = player.getAbsorptionAmount();
        double newAbs = Math.min(abs + 4, 8);

        if (newAbs > abs) {
            Bukkit.getScheduler().runTask(Pit.instance, () -> {
                PlayerUtil.setAbs(player, newAbs);
            });
        }

        PlayerUtil.addHealth(player, 2);
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 160, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 160, 0));
    }

    public static void giveConsumableOnKill(Player player, String nbtKey, String itemId, ItemStack itemCreator, int maxStack) {
        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (itemId.equals(NBTHelper.getString(item, nbtKey))) {
                count += item.getAmount();
            }
        }

        if (count >= maxStack) {
            return;
        }

        for (ItemStack item : player.getInventory().getContents()) {
            if (itemId.equals(NBTHelper.getString(item, nbtKey))) {
                item.setAmount(item.getAmount() + 1);
                return;
            }
        }

        ItemStack newItem = itemCreator.clone();
        newItem.setAmount(1);
        player.getInventory().addItem(newItem);
    }

    public static void clearCooldown(Player player) {
        cooldowns.remove(player.getUniqueId());
    }
}
