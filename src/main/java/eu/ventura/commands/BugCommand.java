package eu.ventura.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import eu.ventura.menu.BugGUI;
import eu.ventura.model.BugModel;
import eu.ventura.model.CommandCooldownModel;
import eu.ventura.service.BugService;
import eu.ventura.util.PlayerUtil;
import org.bukkit.entity.Player;

/**
 * author: ekkoree
 * created at: 1/22/2026
 */
@CommandAlias("bug")
public class BugCommand extends BaseCommand {
    private final CommandCooldownModel commandCooldownModel;

    public BugCommand() {
        this.commandCooldownModel = CommandCooldownModel.getInstance();
    }

    @Subcommand("report")
    public void report(Player player, String bug) {
        if (commandCooldownModel.isOnCooldown("bug", player)) {
            player.sendMessage("§cYou can only report bugs every 30 seconds!");
            return;
        }

        int id = BugService.getNextId();
        BugModel bugModel = new BugModel(
                id,
                player.getUniqueId(),
                player.getName(),
                PlayerUtil.getRankColor(player),
                bug
        );

        BugService.addBug(bugModel);
        commandCooldownModel.setCooldown("bug", player, 30000);
        player.sendMessage("§aBug report #" + id + " submitted successfully!");
    }

    @CommandPermission("rank.owner")
    @Subcommand("show")
    public void show(Player player) {
        new BugGUI(player).open();
    }
}
