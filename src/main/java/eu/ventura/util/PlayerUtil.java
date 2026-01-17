package eu.ventura.util;

import eu.ventura.java.NewString;
import eu.ventura.model.PlayerModel;
import eu.ventura.service.PlayerService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

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

    public static void setAbs(Player player, float amount) {
        player.setAbsorptionAmount(amount);
    }

    public static void addAbs(Player player, float amount) {
        player.setAbsorptionAmount((float) player.getAbsorptionAmount() + amount);
    }

    public static String getRankColor(Player player) {
        return NewString.of("&7");
    }
}
