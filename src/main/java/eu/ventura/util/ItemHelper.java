package eu.ventura.util;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.common.collect.HashMultimap;
import eu.ventura.Pit;
import eu.ventura.constants.Strings;
import eu.ventura.model.PlayerModel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class ItemHelper {

    public static ItemStack createItem(Material material, String displayName, List<String> lore, boolean hideAttributes) {
        return createItem(material, 1, displayName, lore, hideAttributes);
    }

    public static ItemStack createItem(Material material, int amount, String displayName, List<String> lore, boolean hideAttributes) {
        ItemStack stack = new ItemStack(material, amount);
        return setItemMeta(stack, displayName, lore, true, hideAttributes);
    }

    public static ItemStack createItem(ItemStack stack, int amount) {
        if (stack == null) {
            return null;
        }

        ItemStack clone = stack.clone();
        clone.setAmount(amount);

        ItemMeta meta = clone.getItemMeta();
        if (meta != null) {
            meta.setUnbreakable(true);
            clone.setItemMeta(meta);
        }

        return clone;
    }

    public static ItemStack setItemMeta(ItemStack item, String displayName, List<String> lore, boolean unbreakable, boolean hideAttributes) {
        if (item == null) {
            return null;
        }

        ItemStack clone = item.clone();
        ItemMeta meta = clone.getItemMeta();

        if (meta == null) {
            return clone;
        }

        if (displayName != null) {
            meta.setDisplayName(displayName);
        }

        if (lore != null) {
            meta.setLore(lore);
        }

        meta.setUnbreakable(unbreakable);

        if (hideAttributes) {
            meta.setAttributeModifiers(HashMultimap.create());
            for (ItemFlag flag : ItemFlag.values()) {
                meta.addItemFlags(flag);
            }
        }

        clone.setItemMeta(meta);
        return clone;
    }

    public static ItemStack setLore(ItemStack item, List<String> lore) {
        if (item == null) {
            return null;
        }

        ItemStack clone = item.clone();
        ItemMeta meta = clone.getItemMeta();

        if (meta != null) {
            meta.setLore(lore);
            clone.setItemMeta(meta);
        }

        return clone;
    }

    public static ItemStack setDisplayName(ItemStack item, String displayName) {
        if (item == null) {
            return null;
        }

        ItemStack clone = item.clone();
        ItemMeta meta = clone.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(displayName);
            clone.setItemMeta(meta);
        }

        return clone;
    }

    public static ItemStack glowItem(ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addEnchant(Enchantment.UNBREAKING, 1, true);
            itemStack.setItemMeta(meta);
        }

        return itemStack;
    }

    public static ItemStack getReturnMenu(String panel) {
        return createItem(
                Material.ARROW,
                "§aGo Back",
                List.of("§7To " + panel),
                true
        );
    }

    public static ItemStack getReturnMenu(Player player, String panel) {
        Strings.Language lang = PlayerModel.getInstance(player).language;
        return createItem(
                Material.ARROW,
                Strings.Simple.RENOWN_GO_BACK.get(lang),
                List.of(Strings.Formatted.RENOWN_TO_PANEL.format(lang, panel)),
                true
        );
    }

    public static ItemStack decreaseItemAmount(ItemStack item) {
        if (item == null) {
            return null;
        }

        int amount = item.getAmount() - 1;
        if (amount <= 0) {
            return null;
        }

        ItemStack copy = item.clone();
        copy.setAmount(amount);
        return copy;
    }

    public static int getFreeSlot(Inventory inventory, boolean reverse) {
        int size = inventory.getSize();

        if (reverse) {
            for (int i = 9; i < size; i++) {
                if (inventory.getItem(i) == null) {
                    return i;
                }
            }
            for (int i = 0; i < 9; i++) {
                if (inventory.getItem(i) == null) {
                    return i;
                }
            }
            return -1;
        }

        for (int i = 0; i < 9; i++) {
            if (inventory.getItem(i) == null) {
                return i;
            }
        }

        for (int i = 9; i < size; i++) {
            if (inventory.getItem(i) == null) {
                return i;
            }
        }

        return -1;
    }

    public static Color fromHex(String hex) {
        if (!hex.matches("^#?[0-9A-Fa-f]{6}$")) {
            throw new IllegalArgumentException("Couldn't parse HEX color!");
        }

        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }

        int red = Integer.parseInt(hex.substring(0, 2), 16);
        int green = Integer.parseInt(hex.substring(2, 4), 16);
        int blue = Integer.parseInt(hex.substring(4, 6), 16);

        return Color.fromRGB(red, green, blue);
    }

    public static ItemStack createLeather(Material item, String displayName, List<String> lore, Color color) {
        ItemStack stack = new ItemStack(item);
        LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();

        if (meta != null) {
            meta.setColor(color);

            if (displayName != null) {
                meta.setDisplayName(displayName);
            }

            if (lore != null) {
                meta.setLore(lore);
            }

            meta.setUnbreakable(true);
            stack.setItemMeta(meta);
        }

        return stack;
    }

    public static EquipmentSlot getArmorSlot(Material type) {
        String name = type.toString();

        if (name.endsWith("_HELMET")) {
            return EquipmentSlot.HEAD;
        }
        if (name.endsWith("_CHESTPLATE")) {
            return EquipmentSlot.CHEST;
        }
        if (name.endsWith("_LEGGINGS")) {
            return EquipmentSlot.LEGS;
        }
        if (name.endsWith("_BOOTS")) {
            return EquipmentSlot.FEET;
        }

        return null;
    }

    public static ItemStack createSkull(String texture) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();

        if (meta != null) {
            PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
            profile.setProperty(new ProfileProperty("textures", texture));
            meta.setPlayerProfile(profile);
            skull.setItemMeta(meta);
        }

        return skull;
    }

    public static ItemStack getPlayerHead(UUID uuid) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();

        if (meta != null) {
            meta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
            skull.setItemMeta(meta);
        }

        return skull;
    }
}
