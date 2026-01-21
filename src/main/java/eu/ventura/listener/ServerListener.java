package eu.ventura.listener;

import eu.ventura.constants.Strings;
import eu.ventura.util.NBTHelper;
import org.bukkit.Material;
import org.bukkit.advancement.Advancement;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

/**
* author: ekkoree
* created at: 12/26/2025
  */
public class ServerListener implements Listener {
    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        World world = event.getWorld();
        world.setStorm(false);
        world.setThundering(false);
        event.setCancelled(true);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        ItemStack stack = event.getItemDrop().getItemStack();

        boolean cancel = false;

        if (NBTHelper.hasKey(stack, "pit-drop-whitelist")) {
            event.getItemDrop().remove();
            return;
        }

        if (NBTHelper.hasKey(stack, "pit-default-item")) {
            Material type = stack.getType();
            if (!(type == Material.IRON_BOOTS || type == Material.IRON_LEGGINGS || type == Material.IRON_CHESTPLATE)) {
                cancel = true;
            }
        } else if (NBTHelper.hasKey(stack, "pit-perk-item")
                || NBTHelper.hasKey(stack, "pit-enchant-item")
                || NBTHelper.hasKey(stack, "pit-renown-item")
                || NBTHelper.hasKey(stack, "pit-protected-item")) {
            cancel = true;
            event.getPlayer().sendMessage(Strings.Simple.CANT_DROP.get(event.getPlayer()));
        }

        event.setCancelled(cancel);
    }

    @EventHandler
    public void onAdvancement(PlayerAdvancementDoneEvent event) {
        Advancement advancement = event.getAdvancement();
        event.message(null);
        event.getPlayer().getAdvancementProgress(advancement)
                .getAwardedCriteria()
                .forEach(c -> event.getPlayer().getAdvancementProgress(advancement).revokeCriteria(c));
    }
}
