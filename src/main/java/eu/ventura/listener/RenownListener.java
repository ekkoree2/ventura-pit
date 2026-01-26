package eu.ventura.listener;

import eu.ventura.event.PitKillEvent;
import eu.ventura.event.PitRespawnEvent;
import eu.ventura.model.PlayerModel;
import eu.ventura.renown.RenownShop;
import eu.ventura.service.PlayerService;
import eu.ventura.service.RenownService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * author: ekkoree
 * created at: 1/24/2026
 */
public class RenownListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onKill(PitKillEvent event) {
        Player attacker = event.data.trueAttacker;
        if (attacker == null) return;

        PlayerModel playerModel = PlayerService.getPlayer(attacker);
        if (playerModel == null) return;

        for (RenownShop shop : RenownService.getRenownShop().values()) {
            int tier = playerModel.getRenownPerkTier(shop);
            if (tier > 0) {
                shop.onKill(event, tier);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onRespawn(PitRespawnEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;

        PlayerModel playerModel = PlayerService.getPlayer(player);
        if (playerModel == null) return;

        for (RenownShop shop : RenownService.getRenownShop().values()) {
            int tier = playerModel.getRenownPerkTier(shop);
            if (tier > 0) {
                shop.onRespawn(event, tier);
            }
        }
    }
}
