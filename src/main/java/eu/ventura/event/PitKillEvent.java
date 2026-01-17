package eu.ventura.event;

import eu.ventura.model.DeathModel;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PitKillEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    public final DeathModel data;

    public PitKillEvent(DeathModel data) {
        this.data = data;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}