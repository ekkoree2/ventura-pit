package eu.ventura.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import eu.ventura.Pit;
import eu.ventura.constants.PitEvent;

/**
 * author: ekkoree
 * created at: 1/22/2026
 */
@CommandPermission("rank.owner")
@CommandAlias("event")
public class PitEventCommand extends BaseCommand {
    @Subcommand("stop")
    public void stop() {
        if (Pit.event == null) return;
        Pit.event.stop();
    }

    @Subcommand("rage start")
    public void ragePitStart() {
        PitEvent.RAGE_PIT.instance.schedule();
    }
}
