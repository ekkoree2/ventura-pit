package eu.ventura.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import eu.ventura.model.PlayerModel;
import org.bukkit.entity.Player;

/**
* author: ekkoree
* created at: 1/20/2026
 */
@CommandAlias("pit")
public class StatsCommand extends BaseCommand {
    @Subcommand("gold")
    public void gold(Player target, double value) {
        PlayerModel.getInstance(target).setGold(value);
    }

    @Subcommand("level")
    public void level(Player target, int level) {
        PlayerModel.getInstance(target).setLevel(level);
    }

    @Subcommand("prestige")
    public void prestige(Player target, int prestige) {
        PlayerModel.getInstance(target).setPrestige(prestige);
    }
}
