package eu.ventura.service;

import eu.ventura.enchantment.EnchantItem;
import eu.ventura.enchantment.EnchantRarity;
import eu.ventura.enchantment.PitEnchant;
import eu.ventura.enchantment.sword.Crater;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * author: ekkoree
 * created at: 1/15/2025
 */
public class EnchantmentService {

    private static final Map<String, PitEnchant> enchantments = new HashMap<>();

    static {
        registerEnchantment(new Crater());
    }

    public static void registerEnchantment(PitEnchant enchant) {
        enchantments.put(enchant.getId(), enchant);
    }

    public static PitEnchant getEnchantment(String id) {
        return enchantments.get(id);
    }

    public static Collection<PitEnchant> getEnchantments() {
        return enchantments.values();
    }

    public static <T extends PitEnchant> T getInstance(Class<T> enchantClass) {
        for (PitEnchant enchant : enchantments.values()) {
            if (enchantClass.isInstance(enchant)) {
                return enchantClass.cast(enchant);
            }
        }
        return null;
    }

    public static List<PitEnchant> getEnchantsByRarity(EnchantRarity rarity) {
        List<PitEnchant> result = new ArrayList<>();
        for (PitEnchant enchant : enchantments.values()) {
            if (enchant.getRarity() == rarity) {
                result.add(enchant);
            }
        }
        return result;
    }

    public static List<PitEnchant> getEnchantsByItem(EnchantItem itemType) {
        List<PitEnchant> result = new ArrayList<>();
        for (PitEnchant enchant : enchantments.values()) {
            for (EnchantItem type : enchant.getItemTypes()) {
                if (type == itemType || type == EnchantItem.ALL) {
                    result.add(enchant);
                    break;
                }
            }
        }
        return result;
    }

    public static List<PitEnchant> getPossibleEnchants(ItemStack item) {
        EnchantItem itemType = EnchantItem.fromItemStack(item);
        if (itemType == null) {
            return new ArrayList<>();
        }
        return getEnchantsByItem(itemType);
    }
}
