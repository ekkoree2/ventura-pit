package eu.ventura.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import eu.ventura.event.PlayerVanishEvent;
import eu.ventura.model.PlayerModel;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * author: ekkoree
 * created at: 1/27/2026
 */
@CommandPermission("rank.admin")
@CommandAlias("vanish|v")
public class VanishCommand extends BaseCommand {
    @Default
    public void onVanish(Player player) {
        PlayerModel data = PlayerModel.getInstance(player);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 2, 4.05f);

        boolean result = !data.vanished;
        data.vanished = result;

        player.sendMessage(
                "§e§lVANISH! §7Toggled " + (result ? "§a§lON!" : "§c§lOFF!")
        );

        Bukkit.getPluginManager().callEvent(new PlayerVanishEvent(player, result));
    }
}
