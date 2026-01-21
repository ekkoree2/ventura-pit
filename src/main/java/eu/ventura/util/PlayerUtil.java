package eu.ventura.util;

import eu.ventura.java.NewString;
import eu.ventura.model.AttackModel;
import eu.ventura.model.PlayerModel;
import eu.ventura.service.PlayerService;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.RayTraceResult;

import java.util.Objects;

/**
 * author: ekkoree
 * created at: 12/26/2025
 */
public class PlayerUtil {
    public static String getDisplayName(Player player) {
        PlayerModel model = PlayerService.getPlayer(player);
        return String.format(
                "%s%s %s",
                LevelUtil.getFormattedLevelFromValuesChat(model),
                getRankColor(player),
                player.getName()
        );
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

    public static String getRankColor(Player player) {
        return NewString.of("&7");
    }

    public static ChatColor getRankColorChat(Player player) {
        return ChatColor.GRAY;
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
