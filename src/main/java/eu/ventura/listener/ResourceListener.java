package eu.ventura.listener;

import com.destroystokyo.paper.MaterialTags;
import eu.ventura.Pit;
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
    @EventHandler(priority = EventPriority.LOWEST)
    public void onKill(PitKillEvent event) {
        PlayerModel attackerModel = PlayerModel.getInstance(event.data.trueAttacker);
        int aStreak = attackerModel.streak;
        if (Pit.event != null) {
            aStreak = 0;
        }
        PlayerModel victimModel = PlayerModel.getInstance(event.data.victim);
        int vStreak = victimModel.streak;
        if (Pit.event != null) {
            vStreak = 0;
        }

        if (aStreak <= 3) {
            event.data.xp += 4;
        }

        int levelXpBonus = Math.min(attackerModel.getLevel() / 5, 15);
        int prestigeXpBonus = Math.min((attackerModel.getPrestige() / 5) * 6, 25);
        event.data.xp += levelXpBonus + prestigeXpBonus;

        int lvl = victimModel.getLevel() - attackerModel.getLevel();
        if (lvl > 0) {
            event.data.xp += lvl / 5;
        }

        if (aStreak >= 3 && aStreak <= 4) {
            event.data.xpBonus += 3;
        } else if (aStreak>= 5 && aStreak <= 19) {
            event.data.xpBonus += 5;
        } else if (aStreak >= 20) {
            int bonus = (aStreak / 10) * 3;
            event.data.xpBonus += Math.min(bonus, 30);
        }

        if (vStreak >= 3 && vStreak <= 4) {
            event.data.xp += 15;
        } else if (vStreak >= 5 && vStreak <= 19) {
            event.data.xp += 25;
        } else if (vStreak >= 20) {
            int bonus = (vStreak / 10) * 15;
            event.data.xp += Math.min(bonus, 300);
        }

        if (victimModel.getLevel() <= 20) {
            event.data.xpMultiplier -= 0.1;
        }

        if (aStreak <= 3) {
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
