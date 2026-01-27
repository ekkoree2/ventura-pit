package eu.ventura.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * author: ekkoree
 * created at: 1/27/2026
 */
@AllArgsConstructor
@Getter
public class PlayerVanishEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private boolean state;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
