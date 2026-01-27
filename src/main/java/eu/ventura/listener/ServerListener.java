package eu.ventura.listener;

import eu.ventura.Pit;
import eu.ventura.constants.Strings;
import eu.ventura.event.PitDamageEvent;
import eu.ventura.event.PitKillEvent;
import eu.ventura.util.NBTHelper;
import eu.ventura.util.NBTTag;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ThreadLocalRandom;

/**
* author: ekkoree
* created at: 12/26/2025
  */
public class ServerListener implements Listener {
    private static final int MIN_TICKS = 3600;
    private static final int MAX_TICKS = 14400;

    public ServerListener() {
        schedule();
    }

    private void schedule() {
        int delay = ThreadLocalRandom.current().nextInt(MIN_TICKS, MAX_TICKS + 1);
        new BukkitRunnable() {
            @Override
            public void run() {
                remind();
                schedule();
            }
        }.runTaskLater(Pit.instance, delay);
    }

    private void remind() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(Strings.Simple.NOTE_BUG.get(player));
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        World world = event.getWorld();
        world.setStorm(false);
        world.setThundering(false);
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onKill(PitKillEvent event) {
        if (Pit.event != null) {
            Pit.event.onKill(event);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamage(PitDamageEvent event) {
        if (Pit.event != null) {
            Pit.event.onDamage(event);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        if (Pit.event != null) {
            Pit.event.onJoin(event);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        if (Pit.event != null) {
            Pit.event.onQuit(event);
        }
    }

    @EventHandler
    public void onEventItemInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        String eventId = NBTHelper.getString(item, NBTTag.EVENT_ITEM.getValue());
        if (eventId == null) {
            return;
        }
        if (Pit.event != null && Pit.event.getEventId().equals(eventId)) {
            Pit.event.onInteract(event);
        }
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
