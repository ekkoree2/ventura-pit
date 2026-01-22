package eu.ventura.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import eu.ventura.Pit;
import eu.ventura.constants.Strings;
import eu.ventura.java.NewString;
import eu.ventura.model.CommandCooldownModel;
import eu.ventura.model.PlayerModel;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * author: ekkoree
 * created at: 1/22/2026
 */
@CommandAlias("lang")
public class LangCommand extends BaseCommand {
    private final CommandCooldownModel commandCooldownModel;

    public LangCommand() {
        this.commandCooldownModel = CommandCooldownModel.getInstance();
    }

    @Subcommand("en")
    public void english(Player player) {
        this.change(player, Strings.Language.ENGLISH);
    }

    @Subcommand("pl")
    public void pl(Player player) {
        this.change(player, Strings.Language.POLISH);
    }

    private void change(Player player, Strings.Language language) {
        if (commandCooldownModel.isOnCooldown("lang", player)) {
            player.sendMessage(Strings.Simple.LANG_CD.get(player));
            return;
        }

        PlayerModel model = PlayerModel.getInstance(player);
        if (model.language == language) {
            player.sendMessage(Strings.Simple.LANG_ERROR.get(player));
            return;
        }
        model.language = language;
        model.save();
        Bukkit.getScheduler().runTask(Pit.instance, () -> player.kick(Component.text(NewString.of("&cPlease rejoin to affect the changes!"))));
    }
}
