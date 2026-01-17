package eu.ventura.event;

import eu.ventura.constants.RespawnReason;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class PitRespawnEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final RespawnReason reason;
    @Setter
    private Location spawnLocation;

    public PitRespawnEvent(Player player, RespawnReason reason) {
        this.player = player;
        this.reason = reason;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
