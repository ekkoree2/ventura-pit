package eu.ventura.util;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.Set;

/**
 * author: ekkoree
 * created at: 1/15/2025
 */
public class NBTHelper {

    public static ItemStack setString(ItemStack item, String key, String value) {
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setString(key, value);
        return nbtItem.getItem();
    }

    public static ItemStack setBoolean(ItemStack item, String key, Boolean value) {
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setBoolean(key, value);
        return nbtItem.getItem();
    }

    public static String getString(ItemStack item, String key) {
        if (item == null || item.getType() == Material.AIR) {
            return null;
        }
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.getString(key);
    }

    public static Integer getInteger(ItemStack item, String key) {
        if (item == null || item.getType() == Material.AIR) {
            return null;
        }
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.getInteger(key);
    }

    public static ItemStack setInteger(ItemStack item, String key, int value) {
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setInteger(key, value);
        return nbtItem.getItem();
    }

    public static boolean hasKey(ItemStack item, String key) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.hasKey(key);
    }

    public static Set<String> getKeys(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return Collections.emptySet();
        }
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.getKeys();
    }

    public static boolean getBoolean(ItemStack item, String key) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.getBoolean(key);
    }

    public static ItemStack removeKey(ItemStack item, String key) {
        if (item == null || item.getType() == Material.AIR) {
            return item;
        }
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.removeKey(key);
        return nbtItem.getItem();
    }

    public static Double getDouble(ItemStack item, String key) {
        if (item == null || item.getType() == Material.AIR) {
            return null;
        }
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.getDouble(key);
    }

    public static ItemStack setDouble(ItemStack item, String key, double value) {
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setDouble(key, value);
        return nbtItem.getItem();
    }

    public static Long getLong(ItemStack item, String key) {
        if (item == null || item.getType() == Material.AIR) {
            return null;
        }
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.getLong(key);
    }

    public static ItemStack setLong(ItemStack item, String key, long value) {
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setLong(key, value);
        return nbtItem.getItem();
    }
}
