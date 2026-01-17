package eu.ventura.listener;

import eu.ventura.constants.Sounds;
import eu.ventura.constants.Strings;
import eu.ventura.event.PitKillEvent;
import eu.ventura.model.DeathModel;
import eu.ventura.model.PlayerModel;
import eu.ventura.service.PlayerService;
import eu.ventura.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * author: ekkoree
 * created at: 01/16/2025
 */
public class BountyListener implements Listener {

    private final Map<UUID, Long> timeTracker = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onKill(PitKillEvent event) {
        DeathModel deathModel = event.data;
        Player killer = deathModel.trueAttacker;
        Player victim = deathModel.victim;

        if (killer == null || victim == null) {
            return;
        }

        PlayerModel killerModel = PlayerService.getPlayer(killer);
        PlayerModel victimModel = PlayerService.getPlayer(victim);

        timeTracker.putIfAbsent(killer.getUniqueId(), System.currentTimeMillis());
        long since = (long) ((System.currentTimeMillis() - timeTracker.get(killer.getUniqueId())) * 0.001);

        int bounty = calculateBounty(killer, killerModel.multiKills, since);

        if (killerModel.getBounty() + bounty > killerModel.getMaxBounty()) {
            bounty = killerModel.getMaxBounty() - killerModel.getBounty();
        }

        if (bounty != 0) {
            Strings.Simple action = killerModel.getBounty() == 0 ? Strings.Simple.BOUNTY_ACTION_OF : Strings.Simple.BOUNTY_ACTION_BUMP;
            String message = Strings.Formatted.BOUNTY.format(killer, action.get(killerModel.getLanguage()), bounty, PlayerUtil.getDisplayName(killer));
            Bukkit.broadcastMessage(message);
            killerModel.setBounty(killerModel.getBounty() + bounty);
            Sounds.BOUNTY.play(killer);
        }

        if (victimModel.getBounty() > 0) {
            String message = Strings.Formatted.BOUNTY_CLAIMED.format(killer, PlayerUtil.getDisplayName(killer), PlayerUtil.getDisplayName(victim), NumberFormat.getInstance().format(victimModel.getBounty()));
            Bukkit.broadcastMessage(message);
            deathModel.gold += victimModel.getBounty();
            victimModel.setBounty(0);
        }

        timeTracker.put(killer.getUniqueId(), System.currentTimeMillis());
    }

    private int calculateBounty(Player player, int streak, long secondsBetweenKills) {
        if (!randomBounty(player, streak, secondsBetweenKills)) {
            return 0;
        }

        if (streak < 10 && secondsBetweenKills < 5) return 25;
        if (streak < 25) return secondsBetweenKills < 5 ? 75 : 50;
        if (streak < 50 && secondsBetweenKills < 5) return 100;
        if (streak < 100 && secondsBetweenKills < 1) return 125;
        return 150;
    }

    private boolean randomBounty(Player player, int streak, long secondsBetweenKills) {
        if (streak < 6) return false;

        double base = Math.min(streak / 160.0, 0.4);
        double speed = 1.0 + Math.max(0, (4.0 - secondsBetweenKills)) * 0.03;
        double multi = 0.6;

        return ThreadLocalRandom.current().nextDouble() < base * speed * multi;
    }
}
