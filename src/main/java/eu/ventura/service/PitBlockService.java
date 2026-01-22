package eu.ventura.service;

import eu.ventura.Pit;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * author: ekkoree
 * created at: 1/22/2026
 */
public class PitBlockService {
    private static final File file = new File(Pit.instance.getDataFolder(), "blocks.yml");
    private static FileConfiguration config;

    @SuppressWarnings("all")
    public static void init() {
        if (!Pit.instance.getDataFolder().exists()) {
            Pit.instance.getDataFolder().mkdirs();
        }
        if (file.exists()) {
            config = YamlConfiguration.loadConfiguration(file);
            removeAllBlocks(null);
            file.delete();
        }
        config = new YamlConfiguration();
    }

    public static void saveBlock(Location location, long expireAfter) {
        String key = locationToString(location);
        ConfigurationSection section = config.createSection(key);
        section.set("world", location.getWorld().getName());
        section.set("x", location.getBlockX());
        section.set("y", location.getBlockY());
        section.set("z", location.getBlockZ());
        section.set("expireAfter", expireAfter);
        saveConfig();
    }

    public static void removeBlock(Location location) {
        String key = locationToString(location);
        config.set(key, null);
        saveConfig();
    }

    public static void removeAllBlocks(Material filter) {
        List<String> keys = new ArrayList<>(config.getKeys(false));
        for (String key : keys) {
            ConfigurationSection section = config.getConfigurationSection(key);
            if (section == null) continue;

            String name = section.getString("world");
            if (name == null) continue;
            int x = section.getInt("x");
            int y = section.getInt("y");
            int z = section.getInt("z");

            Location location = new Location(Bukkit.getWorld(name), x, y, z);
            if (filter == null || location.getBlock().getType() == filter) {
                location.getBlock().setType(Material.AIR, false);
                config.set(key, null);
            }
        }
        saveConfig();
    }

    public static boolean isPitBlock(Location location) {
        return config.contains(locationToString(location));
    }

    private static String locationToString(Location location) {
        return location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ();
    }

    private static void saveConfig() {
        try {
            config.save(file);
        } catch (IOException ignored) {}
    }
}
