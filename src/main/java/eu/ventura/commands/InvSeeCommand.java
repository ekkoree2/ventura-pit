package eu.ventura.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import eu.ventura.menu.InvSeeGUI;
import org.bukkit.entity.Player;

/**
 * author: ekkoree
 * created at: 1/27/2026
 */
@CommandPermission("rank.admin")
@CommandAlias("invsee")
public class InvSeeCommand extends BaseCommand {
    @Default
    public void onInvsee(Player player, OnlinePlayer target) {
        Player victim = target.getPlayer();
        new InvSeeGUI(player, victim).open();
    }
}
