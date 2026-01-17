package eu.ventura.event;

import eu.ventura.model.DeathModel;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PitAssistEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    public final DeathModel deathModel;
    public final Player assister;
    public final double percentage;

    public PitAssistEvent(DeathModel deathModel, Player assister, double percentage) {
        this.deathModel = deathModel;
        this.assister = assister;
        this.percentage = percentage;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
