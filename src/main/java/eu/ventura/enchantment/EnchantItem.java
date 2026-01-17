package eu.ventura.enchantment;

import org.bukkit.inventory.ItemStack;

/**
 * author: ekkoree
 * created at: 1/15/2025
 */
public enum EnchantItem {
    PANTS,
    SWORD,
    BOW,
    ALL,
    SPECIAL;

    public static EnchantItem fromItemStack(ItemStack item) {
        if (item == null) return null;
        String type = item.getType().toString().toLowerCase();
        if (type.contains("leggings") || type.contains("pants")) return PANTS;
        if (type.contains("sword")) return SWORD;
        if (type.contains("bow")) return BOW;
        return null;
    }
}
