package eu.ventura.service;

import eu.ventura.Pit;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

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
        PlayerService.players.values().forEach(model -> {
            if (model.combatTime > 0) {
                Bukkit.getScheduler().runTask(Pit.instance, () -> {
                    model.combatTime--;

                    if (model.combatTime == 0) {
                        model.updateStatus();
                        model.lastAttacker = null;
                    }
                });
            }
        });
    }
}
