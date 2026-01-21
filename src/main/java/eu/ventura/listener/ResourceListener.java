package eu.ventura.listener;

import eu.ventura.event.PitKillEvent;
import eu.ventura.model.PlayerModel;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

/**
 * author: ekkoree
 * created at: 1/19/2026
 */
public class ResourceListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onKill(PitKillEvent event) {
        PlayerModel attackerModel = PlayerModel.getInstance(event.data.trueAttacker);
        PlayerModel victimModel = PlayerModel.getInstance(event.data.victim);

        if (attackerModel.streak <= 3) {
            event.data.xp += 4;
        }

        int lvl = victimModel.getLevel() - attackerModel.getLevel();
        if (lvl > 0) {
            event.data.xp += lvl / 5;
        }

        if (attackerModel.streak >= 3 && attackerModel.streak <= 4) {
            event.data.xpBonus += 3;
        } else if (attackerModel.streak >= 5 && attackerModel.streak <= 19) {
            event.data.xpBonus += 5;
        } else if (attackerModel.streak >= 20) {
            int bonus = (attackerModel.streak / 10) * 3;
            event.data.xpBonus += Math.min(bonus, 30);
        }

        if (victimModel.streak >= 3 && victimModel.streak <= 4) {
            event.data.xp += 15;
        } else if (victimModel.streak >= 5 && victimModel.streak <= 19) {
            event.data.xp += 25;
        } else if (victimModel.streak >= 20) {
            int bonus = (victimModel.streak / 10) * 15;
            event.data.xp += Math.min(bonus, 300);
        }

        if (victimModel.getLevel() <= 20) {
            event.data.xpMultiplier -= 0.1;
        }

        if (attackerModel.streak <= 3) {
            event.data.gold += 4;
        }

        event.data.gold += getArmorGoldBonus(event.data.trueAttacker, event.data.victim);

        if (victimModel.getLevel() <= 20) {
            event.data.goldMultiplier -= 0.1;
        }
    }

    private double getArmorGoldBonus(Player killer, Player victim) {
        int killerValue = getArmorValue(killer);
        int victimValue = getArmorValue(victim);
        int diff = victimValue - killerValue;
        return diff > 0 ? Math.min(diff, 16) : 0;
    }

    private int getArmorValue(Player player) {
        int value = 0;
        for (ItemStack item : player.getInventory().getArmorContents()) {
            if (item == null) continue;

            Material type = item.getType();
            switch (type) {
                case DIAMOND_HELMET:
                case DIAMOND_CHESTPLATE:
                case DIAMOND_LEGGINGS:
                case DIAMOND_BOOTS:
                    value += 4;
                    break;
                case IRON_HELMET:
                case IRON_CHESTPLATE:
                case IRON_LEGGINGS:
                case IRON_BOOTS:
                    value += 2;
                    break;
                case CHAINMAIL_HELMET:
                    value += 1;
                    break;
            }
        }
        return value;
    }
}
