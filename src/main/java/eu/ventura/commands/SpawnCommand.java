package eu.ventura.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import eu.ventura.constants.RespawnReason;
import eu.ventura.constants.Sounds;
import eu.ventura.constants.Status;
import eu.ventura.constants.Strings;
import eu.ventura.event.PitRespawnEvent;
import eu.ventura.model.CommandCooldownModel;
import eu.ventura.model.PlayerModel;
import eu.ventura.util.PlayerUtil;
import eu.ventura.util.RegionHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * author: ekkoree
 * created at: 1/15/2025
 */
public class SpawnCommand extends BaseCommand {
    private final CommandCooldownModel commandCooldownModel;

    public SpawnCommand() {
        this.commandCooldownModel = CommandCooldownModel.getInstance();
    }

    @CommandAlias("spawn|respawn")
    public void spawn(Player player) {
        if (commandCooldownModel.isOnCooldown("spawn", player)) {
            player.sendMessage(Strings.Simple.RESPAWN_COOLDOWN.get(player));
            return;
        }

        PlayerModel playerModel = PlayerModel.getInstance(player);
        if (playerModel.status == Status.FIGHTING) {
            player.sendMessage(Strings.Formatted.CANT_RESPAWN_FIGHTING.format(player, playerModel.combatTime));
            return;
        }

        if (RegionHelper.isInSpawn(player)) {
            player.sendMessage(Strings.Simple.CANT_RESPAWN_HERE.get(player));
            return;
        }

        commandCooldownModel.setCooldown("spawn", player, 10);

        PitRespawnEvent event = new PitRespawnEvent(player, RespawnReason.RESPAWN);
        Bukkit.getPluginManager().callEvent(event);
    }


    @CommandAlias("oof")
    private void onDeath(Player player) {
        if (commandCooldownModel.isOnCooldown("spawn", player)) {
            player.sendMessage(Strings.Simple.OOF_CD.get(player));
            Sounds.ENDERMAN_NO.play(player);
            return;
        }
        if (RegionHelper.isInSpawn(player)) {
            player.sendMessage(Strings.Simple.OOF_NO.get(player));
            Sounds.ENDERMAN_NO.play(player);
            return;
        }
        PlayerUtil.killPlayer(player);
        commandCooldownModel.setCooldown("spawn", player, 10);
    }
}
