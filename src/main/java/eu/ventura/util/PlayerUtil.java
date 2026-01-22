package eu.ventura.util;

import eu.ventura.Pit;
import eu.ventura.events.major.impl.RagePit;
import eu.ventura.model.PlayerModel;
import eu.ventura.service.PlayerService;
import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

import java.util.Objects;
import java.util.UUID;

/**
 * author: ekkoree
 * created at: 12/26/2025
 */
@SuppressWarnings("deprecation")
public class PlayerUtil {
    private static final LuckPerms luckPerms = LuckPermsProvider.get();

    public static String getDisplayName(Player player) {
        PlayerModel model = PlayerService.getPlayer(player);
        return String.format(
                "%s%s %s",
                LevelUtil.getFormattedLevelFromValuesChat(model),
                getRankColor(player),
                player.getName()
        );
    }

    @SuppressWarnings("ALL")
    public static void updateMaxHealth(Player player, boolean heal) {
        double base = 20.0;
        if (Pit.event instanceof RagePit) {
            base *= 2;
        }
        player.setMaxHealth(base);
        if (heal) {
            player.setHealth(base);
        }
    }

    public static void die(Player victim) {
        victim.damage(Double.MAX_VALUE);
    }

    public static void sendTitle(Player player, String title, String subtitle) {
        player.sendTitle(title, subtitle, 10, 40, 10);
    }

    public static void displayIndicator(Player player, String message) {
        player.sendActionBar(Component.text(message));
    }

    public static void addHealth(Player player, double amount) {
        double value = Math.min(player.getMaxHealth(), player.getHealth() + amount);
        player.setHealth(value);
    }

    public static void setAbs(Player player, double amount) {
        Objects.requireNonNull(player.getAttribute(Attribute.MAX_ABSORPTION)).setBaseValue(999999);
        player.setAbsorptionAmount(amount);
    }

    public static void addAbs(Player player, double amount) {
        player.setAbsorptionAmount(player.getAbsorptionAmount() + amount);
    }

    private static User getUser(Player player) {
        return luckPerms.getUserManager().getUser(player.getUniqueId());
    }

    public static String getRankColor(Player player) {
        User user = getUser(player);
        if (user == null) return "&7";
        CachedMetaData metaData = user.getCachedData().getMetaData();
        String prefix = metaData.getPrefix();
        if (prefix != null && prefix.length() >= 2) {
            return prefix.substring(0, 2);
        }
        return "&7";
    }

    public static String getRank(Player player) {
        User user = getUser(player);
        if (user == null) return ChatColor.GRAY.toString();
        CachedMetaData metaData = user.getCachedData().getMetaData();
        String prefix = metaData.getPrefix();
        if (prefix == null || prefix.isEmpty()) {
            return ChatColor.GRAY.toString();
        }
        return ChatColor.translateAlternateColorCodes('&', prefix);
    }

    public static ChatColor getRankColorChat(Player player) {
        User user = getUser(player);
        if (user == null) return ChatColor.GRAY;
        CachedMetaData metaData = user.getCachedData().getMetaData();
        String prefix = metaData.getPrefix();
        if (prefix == null || prefix.length() < 2) return ChatColor.GRAY;
        char colorChar = prefix.charAt(1);
        return switch (colorChar) {
            case '0' -> ChatColor.BLACK;
            case '1' -> ChatColor.DARK_BLUE;
            case '2' -> ChatColor.DARK_GREEN;
            case '3' -> ChatColor.DARK_AQUA;
            case '4' -> ChatColor.DARK_RED;
            case '5' -> ChatColor.DARK_PURPLE;
            case '6' -> ChatColor.GOLD;
            case '7' -> ChatColor.GRAY;
            case '8' -> ChatColor.DARK_GRAY;
            case '9' -> ChatColor.BLUE;
            case 'a' -> ChatColor.GREEN;
            case 'b' -> ChatColor.AQUA;
            case 'c' -> ChatColor.RED;
            case 'd' -> ChatColor.LIGHT_PURPLE;
            case 'e' -> ChatColor.YELLOW;
            case 'f' -> ChatColor.WHITE;
            default -> ChatColor.GRAY;
        };
    }

    public static String getRankColor(UUID uuid) {
        User user = luckPerms.getUserManager().getUser(uuid);
        if (user == null) {
            user = luckPerms.getUserManager().loadUser(uuid).join();
        }
        if (user == null) return "&7";
        CachedMetaData metaData = user.getCachedData().getMetaData();
        String prefix = metaData.getPrefix();
        if (prefix != null && prefix.length() >= 2) {
            return prefix.substring(0, 2);
        }
        return "&7";
    }

    public static boolean isLookingAt(Player player, Entity target, double maxDistance) {
        return isLookingAt(player, target, maxDistance, 0.0);
    }

    public static boolean isLookingAt(Player player, Entity target, double maxDistance, double raySize) {
        Location eyeLocation = player.getEyeLocation();
        RayTraceResult result = player.getWorld().rayTraceEntities(
                eyeLocation,
                eyeLocation.getDirection(),
                maxDistance,
                raySize,
                entity -> entity.equals(target)
        );
        return result != null && target.equals(result.getHitEntity());
    }

    public static boolean isLookingAt(Player player, Entity target) {
        return isLookingAt(player, target, 180);
    }
}
