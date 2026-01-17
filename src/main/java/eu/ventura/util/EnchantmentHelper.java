package eu.ventura.util;

import eu.ventura.constants.Strings;
import eu.ventura.enchantment.EnchantItem;
import eu.ventura.enchantment.EnchantRarity;
import eu.ventura.enchantment.PitEnchant;
import eu.ventura.model.PlayerModel;
import eu.ventura.service.EnchantmentService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * author: ekkoree
 * created at: 1/15/2025
 */
public class EnchantmentHelper {

    public static TextColor getMysticColor(ItemStack item, int tier) {
        return switch (tier) {
            case 1 -> NamedTextColor.GREEN;
            case 2 -> NamedTextColor.YELLOW;
            default -> NamedTextColor.RED;
        };
    }

    public static Map<String, Integer> getEnchants(ItemStack item) {
        Map<String, Integer> enchantments = new HashMap<>();
        if (item == null) {
            return enchantments;
        }

        var allKeys = NBTHelper.getKeys(item);

        for (String key : allKeys) {
            if (key.startsWith(NBTTag.ENCHANT_PREFIX.getValue()) && !key.endsWith("-level")) {
                String enchantId = key.substring(NBTTag.ENCHANT_PREFIX.getValue().length());
                Integer level = NBTHelper.getInteger(item, NBTTag.ENCHANT_PREFIX.of(enchantId + "-level"));
                if (level != null) {
                    enchantments.put(enchantId, level);
                }
            }
        }

        return enchantments;
    }

    public static ItemStack addEnchant(ItemStack item, String enchantId, int level, Player player) {
        if (item == null || item.getAmount() == 0) {
            return item;
        }

        item = NBTHelper.setString(item, NBTTag.ENCHANT_PREFIX.of(enchantId), "true");
        item = NBTHelper.setInteger(item, NBTTag.ENCHANT_PREFIX.of(enchantId + "-level"), level);
        return refreshItem(item, player);
    }

    public static ItemStack removeEnchant(ItemStack item, String enchantId, Player player) {
        if (item == null) {
            return item;
        }

        item = NBTHelper.removeKey(item, NBTTag.ENCHANT_PREFIX.of(enchantId));
        item = NBTHelper.removeKey(item, NBTTag.ENCHANT_PREFIX.of(enchantId + "-level"));
        return refreshItem(item, player);
    }

    public static void setEnchantLevel(ItemStack item, PitEnchant enchant, int level, Player player) {
        if (level <= 0) {
            removeEnchant(item, enchant.getId(), player);
        } else {
            addEnchant(item, enchant.getId(), level, player);
        }
    }

    public static int getEnchantTier(ItemStack item, PitEnchant enchant) {
        Map<String, Integer> enchants = getEnchants(item);
        return enchants.getOrDefault(enchant.getId(), 0);
    }

    public static <T extends PitEnchant> boolean hasEnchant(ItemStack item, Class<T> enchantClass) {
        Map<String, Integer> enchants = getEnchants(item);
        if (enchantClass.getSimpleName().isEmpty()) {
            return false;
        }
        String className = enchantClass.getSimpleName();
        String enchantId = className.substring(0, 1).toLowerCase() + className.substring(1);
        return enchants.containsKey(enchantId);
    }

    public static int getTier(ItemStack item) {
        Integer tier = NBTHelper.getInteger(item, NBTTag.MYSTIC_TIER.getValue());
        return tier != null ? tier : 0;
    }

    public static ItemStack setTier(ItemStack item, int tier, Player player) {
        if (item == null) {
            return item;
        }

        if (tier > 0) {
            item = NBTHelper.setInteger(item, NBTTag.MYSTIC_TIER.getValue(), tier);
        } else {
            item = NBTHelper.removeKey(item, NBTTag.MYSTIC_TIER.getValue());
        }
        return refreshItem(item, player);
    }

    public static boolean isPants(ItemStack item) {
        if (item == null) {
            return false;
        }
        Material type = item.getType();
        return type.name().contains("LEGGINGS");
    }

    public static EnchantItem getItemType(ItemStack item) {
        return EnchantItem.fromItemStack(item);
    }

    public static Color getPantsColor(ItemStack item) {
        if (!isPants(item) || !item.hasItemMeta()) {
            return null;
        }

        var itemMeta = item.getItemMeta();
        if (itemMeta instanceof LeatherArmorMeta leatherMeta) {
            return leatherMeta.getColor();
        }

        return null;
    }

    public static void setPantsColor(ItemStack item, Color color) {
        if (!isPants(item)) {
            return;
        }

        var itemMeta = item.getItemMeta();
        if (itemMeta instanceof LeatherArmorMeta leatherMeta) {
            leatherMeta.setColor(color);
            item.setItemMeta(leatherMeta);
        }
    }

    public static int getLives(ItemStack item) {
        Integer lives = NBTHelper.getInteger(item, NBTTag.MYSTIC_LIVES.getValue());
        return lives != null ? lives : 0;
    }

    public static int getMaxLives(ItemStack item) {
        Integer maxLives = NBTHelper.getInteger(item, NBTTag.MYSTIC_MAX_LIVES.getValue());
        return maxLives != null ? maxLives : 0;
    }

    public static ItemStack setLives(ItemStack item, int lives, int maxLives, Player player) {
        if (item == null) {
            return item;
        }

        if (lives > 0) {
            item = NBTHelper.setInteger(item, NBTTag.MYSTIC_LIVES.getValue(), lives);
        } else {
            item = NBTHelper.removeKey(item, NBTTag.MYSTIC_LIVES.getValue());
        }

        if (maxLives > 0) {
            item = NBTHelper.setInteger(item, NBTTag.MYSTIC_MAX_LIVES.getValue(), maxLives);
        } else {
            item = NBTHelper.removeKey(item, NBTTag.MYSTIC_MAX_LIVES.getValue());
        }

        return refreshItem(item, player);
    }

    public static boolean takeLife(ItemStack item, Player player) {
        int lives = getLives(item);
        int maxLives = getMaxLives(item);

        if (lives <= 0) {
            return false;
        }

        setLives(item, lives - 1, maxLives, player);
        return true;
    }

    public static String getItemPrefix(ItemStack item) {
//        if (!NBTHelper.hasKey(item, NBTTag.MYSTIC_ITEM.getValue())) {
//            return "";
//        }
//
//        int tier = getTier(item);
//        return switch (tier) {
//            case 1 -> "§e✯ Rare ";
//            case 2 -> "§b✯✯ Legendary ";
//            case 3 -> "§5✯✯✯ Mythic ";
//            default -> "";
//        };
        return null;
    }

    public static <T extends PitEnchant> int getEnchantLevel(ItemStack item, Class<T> enchantClass) {
        Map<String, Integer> enchants = getEnchants(item);
        if (enchantClass.getSimpleName().isEmpty()) {
            return 0;
        }
        String className = enchantClass.getSimpleName();
        String enchantId = className.substring(0, 1).toLowerCase() + className.substring(1);
        return enchants.getOrDefault(enchantId, 0);
    }

    public static ItemStack refreshItem(ItemStack item, Player player) {
        if (item == null) {
            return null;
        }

        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            return item;
        }

        Integer tier = NBTHelper.getInteger(item, NBTTag.MYSTIC_TIER.getValue());
        EnchantItem itemType = getItemType(item);

        TextColor tierColor = getMysticColor(item, tier != null ? tier : 0);
        String displayName = getItemPrefix(item);

        if (displayName == null) {
            displayName = "";
        }

        Strings.Language language = PlayerModel.getInstance(player).getLanguage();

        if (tier != null && tier > 0) {
            String prefix = displayName;
            String tierRoman = MathUtil.roman(tier, MathUtil.Symbols.FANCY);
            if (itemType == EnchantItem.SWORD) {
                String itemTypeStr = Strings.Simple.ITEM_SWORD.get(language);
                displayName = Strings.Formatted.TIER_ITEM.format(language, prefix, tierRoman, itemTypeStr);
            } else if (itemType == EnchantItem.BOW) {
                String itemTypeStr = Strings.Simple.ITEM_BOW.get(language);
                displayName = Strings.Formatted.TIER_ITEM.format(language, prefix, tierRoman, itemTypeStr);
            } else if (itemType == EnchantItem.PANTS) {
                String pantsColor = NBTHelper.getString(item, NBTTag.PANTS_COLOR.getValue());
                if (pantsColor != null) {
                    String itemTypeStr = pantsColor + " " + Strings.Simple.ITEM_PANTS.get(language);
                    displayName = Strings.Formatted.TIER_ITEM.format(language, prefix, tierRoman, itemTypeStr);
                }
            }
        }

        if (!displayName.isEmpty()) {
            itemMeta.displayName(Component.text(displayName).color(tierColor).decoration(TextDecoration.ITALIC, false));
        }

        List<Component> lore = new ArrayList<>();
        Map<String, Integer> enchants = getEnchants(item);

        int currentLives = getLives(item);
        int maxLives = getMaxLives(item);

        if (maxLives > 0) {
            lore.add(Component.text(Strings.Formatted.LIVES.format(language, "", currentLives, maxLives)));
            lore.add(Component.text(""));
        }

        if (!enchants.isEmpty()) {
            List<Map.Entry<PitEnchant, Integer>> sorted = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : enchants.entrySet()) {
                PitEnchant enchant = EnchantmentService.getEnchantment(entry.getKey());
                if (enchant != null) {
                    sorted.add(Map.entry(enchant, entry.getValue()));
                }
            }
            sorted.sort(Comparator.comparingInt(e -> e.getKey().getPriority()));

            int i = 0;
            int size = sorted.size();
            for (Map.Entry<PitEnchant, Integer> entry : sorted) {
                PitEnchant enchant = entry.getKey();
                int level = entry.getValue();

                String levelStr = level > 1 ? " " + MathUtil.roman(level) : "";
                String enchantName = enchant.getFormattedName(language) + levelStr;

                lore.add(Component.text(enchantName));

                LoreBuilder loreBuilder = enchant.getDescription(level, language);
                for (String line : loreBuilder.compile()) {
                    lore.add(Component.text(line));
                }

                if (i < size - 1) {
                    lore.add(Component.text(""));
                }
                i++;
            }

            lore.add(Component.text(""));
        }

        boolean isMysticSword = NBTHelper.hasKey(item, NBTTag.MYSTIC_ITEM.getValue()) && itemType == EnchantItem.SWORD;
        if (isMysticSword && enchants.isEmpty()) {
            lore.add(Component.text(Strings.Formatted.MYSTIC_WELL_USAGE.format(language)));
            lore.add(Component.text(""));
        }

        if (itemType == EnchantItem.SWORD) {
            lore.add(Component.text(Strings.Formatted.SWORD_ATTACK_DAMAGE.format(language)));
        } else if (itemType == EnchantItem.PANTS) {
            String pantsColor = NBTHelper.getString(item, NBTTag.PANTS_COLOR.getValue());
            if (pantsColor != null) {
                lore.add(Component.text(pantsColor + Strings.Formatted.PANTS_STRENGTH_IRON.format(language)));
            }
        }

        itemMeta.lore(lore.isEmpty() ? null : lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public static void syncInventory(Player player) {
        var inventory = player.getInventory();
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if ((NBTHelper.hasKey(stack, NBTTag.MYSTIC_ITEM.getValue()) || NBTHelper.hasKey(stack, NBTTag.MYSTIC_TIER.getValue()))) {
                inventory.setItem(i, refreshItem(stack, player));
            }
        }

        ItemStack[] armorContents = inventory.getArmorContents();
        for (int i = 0; i < armorContents.length; i++) {
            ItemStack stack = armorContents[i];
            if ((NBTHelper.hasKey(stack, NBTTag.MYSTIC_ITEM.getValue()) || NBTHelper.hasKey(stack, NBTTag.MYSTIC_TIER.getValue()))) {
                armorContents[i] = refreshItem(stack, player);
            }
        }
        inventory.setArmorContents(armorContents);
    }
}
