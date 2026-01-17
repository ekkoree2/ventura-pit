package eu.ventura.model;

import eu.ventura.enchantment.*;
import eu.ventura.service.EnchantmentService;
import eu.ventura.util.EnchantmentHelper;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * author: ekkoree
 * created at: 1/15/2025
 */
@Getter
public class AttackEnchantModel {
    private final Map<Player, Map<String, Integer>> enchantments;
    private final Set<String> enchantmentsToRemove;
    private final Player victim;
    private final Entity attacker;
    @Setter
    private Player trueAttacker;

    public AttackEnchantModel(Player victim, Entity attacker, Player trueAttacker) {
        this.victim = victim;
        this.attacker = attacker;
        this.trueAttacker = trueAttacker;
        this.enchantments = new HashMap<>();
        this.enchantmentsToRemove = new HashSet<>();
    }

    public void collectEnchantments() {
        if (victim != null) {
            collectPlayerEnchantments(victim);
        }
        if (trueAttacker != null && trueAttacker != victim) {
            collectPlayerEnchantments(trueAttacker);
        }
    }

    private void collectPlayerEnchantments(Player player) {
        Map<String, Integer> playerEnchants = new HashMap<>();

        ItemStack weapon = player.getInventory().getItemInMainHand();
        collectFromItem(weapon, playerEnchants);

        for (ItemStack armor : player.getInventory().getArmorContents()) {
            collectFromItem(armor, playerEnchants);
        }

        enchantments.put(player, playerEnchants);
    }

    private void collectFromItem(ItemStack item, Map<String, Integer> target) {
        if (item == null) {
            return;
        }

        Map<String, Integer> itemEnchants = EnchantmentHelper.getEnchants(item);
        target.putAll(itemEnchants);
    }

    public void applyEnchantments(BiConsumer<PitEnchant, Integer> enchantConsumer, EnchantType type) {
        List<EnchantApplication> toApply = new ArrayList<>();

        for (Map.Entry<Player, Map<String, Integer>> entry : enchantments.entrySet()) {
            Player player = entry.getKey();
            Map<String, Integer> playerEnchants = entry.getValue();

            for (Map.Entry<String, Integer> enchantEntry : playerEnchants.entrySet()) {
                String enchantId = enchantEntry.getKey();
                int level = enchantEntry.getValue();

                PitEnchant enchant = EnchantmentService.getEnchantment(enchantId);
                if (enchant == null) {
                    continue;
                }

                EnchantType enchantType = enchant.getType();
                if (type != EnchantType.BOTH && enchantType != EnchantType.BOTH && enchantType != type) {
                    continue;
                }

                boolean shouldApply = switch (type) {
                    case OFFENSIVE -> player.equals(trueAttacker);
                    case DEFENSIVE -> player.equals(victim);
                    case BOTH -> true;
                };

                if (!shouldApply) {
                    continue;
                }

                toApply.add(new EnchantApplication(enchant, level, player, enchant.getPriority()));
            }
        }

        toApply.sort(Comparator.comparingInt(EnchantApplication::priority));

        for (EnchantApplication application : toApply) {
            enchantConsumer.accept(application.enchant, application.level);
        }

        enchantmentsToRemove.forEach(enchantmentId -> {
            for (Map<String, Integer> playerEnchants : enchantments.values()) {
                playerEnchants.remove(enchantmentId);
            }
        });
    }

    private record EnchantApplication(PitEnchant enchant, int level, Player player, int priority) {
    }
}
