package eu.ventura.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Optional;
import eu.ventura.menu.StatsGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
* author: ekkoree
* created at: 1/20/2026
 */
public class StatsCommand extends BaseCommand {
    @CommandAlias("stats")
    public void stats(Player player, @Optional String targetName) {
        Player target = player;
        if (targetName != null) {
            Player targetPlayer = Bukkit.getPlayer(targetName);
            if (targetPlayer == null) {
                player.sendMessage("§cPlayer not found!");
                return;
            }
            target = targetPlayer;
        }
        new StatsGUI(player, target).open();
    }
}
