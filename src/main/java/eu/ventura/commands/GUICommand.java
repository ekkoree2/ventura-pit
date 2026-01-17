package eu.ventura.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import eu.ventura.menu.PermanentGUI;
import org.bukkit.entity.Player;

@CommandAlias("gui")
public class GUICommand extends BaseCommand {
    @Default
    public void gui(Player player) {
        player.sendMessage("§cUsage: /gui <type>");
    }

    @Subcommand("perk")
    public void perk(Player player) {
        new PermanentGUI(player).open();
    }
}
