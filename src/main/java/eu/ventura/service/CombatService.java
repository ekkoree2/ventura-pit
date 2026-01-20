package eu.ventura.service;

import eu.ventura.Pit;
import eu.ventura.model.PlayerModel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.ConcurrentHashMap;

/**
 * author: ekkoree
 * created at: 1/15/2025
 */
public class CombatService implements Runnable {
    private static CombatService instance;
    private BukkitTask task;

    public static CombatService getInstance() {
        if (instance == null) {
            instance = new CombatService();
        }
        return instance;
    }

    public void start() {
        task = Bukkit.getScheduler().runTaskTimer(Pit.instance, this, 20L, 20L);
    }

    public void stop() {
        if (task != null) {
            task.cancel();
        }
    }

    @Override
    public void run() {
        ConcurrentHashMap<Player, PlayerModel> players = new ConcurrentHashMap<>(PlayerService.players);

        players.values().forEach(player -> {
            if (player.combatTime > 0) {
                Bukkit.getScheduler().runTask(Pit.instance, () -> {
                    player.combatTime--;

                    if (player.combatTime == 0) {
                        player.updateStatus();
                        player.lastAttacker = null;
                    }
                });
            }
        });
    }
}
