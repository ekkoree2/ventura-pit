package eu.ventura.listener;

import de.oliver.fancynpcs.api.events.NpcInteractEvent;
import eu.ventura.Pit;
import eu.ventura.constants.PitNPC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.World;

/**
 * author: ekkoree
 * created at: 12/26/2025
 */
public class ServerListener implements Listener {
    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        World world = event.getWorld();
        world.setStorm(false);
        world.setThundering(false);
        event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(NpcInteractEvent event) {
        for (PitNPC pitNPC : PitNPC.values()) {
            String name = event.getNpc().getData().getName();
            if (pitNPC.getSkin().equals(name)) {
                pitNPC.getTask().accept(event.getPlayer());
                break;
            }
        }
    }
}
