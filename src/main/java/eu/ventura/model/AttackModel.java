package eu.ventura.model;

import eu.ventura.Pit;
import eu.ventura.util.PlayerUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * author: ekkoree
 * created at: 1/15/2025
 */
@SuppressWarnings("deprecation")
public class AttackModel {
    public final EntityDamageByEntityEvent event;
    public final Player victim;
    public final Entity attacker;
    public Player trueAttacker;
    public final EntityDamageEvent.DamageCause cause;
    public boolean cancelled;
    public double damage;
    public double damageMultiplier = 1.0;
    public double bountyHunterMultiplier = 0.0;
    public double trueDamage;
    public double trueTrueDamage;
    public double attackerHealthGained;
    public float attackerAbsorptionGained;
    public double reflectTrueDamage;
    public double reflectTrueTrueDamage;
    public int extraGold;
    public String extraMessageContent = "";
    public double victimHealthGained;

    public AttackModel(EntityDamageByEntityEvent event, Player victim, Entity attacker) {
        this.event = event;
        this.victim = victim;
        this.attacker = attacker;
        this.trueAttacker = attacker instanceof Player ? (Player) attacker : null;
        this.cause = event.getCause();
        this.damage = event.getDamage();
    }

    public boolean isFalling(Player player) {
        if (player.getFallDistance() <= 0) {
            return false;
        }

        if (!victim.isOnGround()) {
            return false;
        }

        double attackerY = player.getLocation().getY();
        double victimY = victim.getLocation().getY();

        return attackerY > victimY + victim.getEyeHeight();
    }

    public double getFinalDamage() {
        return damage * (damageMultiplier + bountyHunterMultiplier);
    }

    public boolean isOnGround(Player player) {
        return player.isOnGround();
    }

    public boolean isCritical(Player player) {
        return !player.isOnGround() && player.getFallDistance() > 0;
    }

    public void finalApply() {
        if (event.isCancelled() || cancelled) {
            return;
        }

        if (victim != null) {
            PlayerUtil.addHealth(victim, victimHealthGained);
        }
        if (trueAttacker != null) {
            PlayerUtil.addHealth(trueAttacker, attackerHealthGained);
            PlayerUtil.addAbs(trueAttacker, attackerAbsorptionGained);
        }

        double total = getFinalDamage();
        event.setDamage(total);

        int originalHealth = victim != null ? (int) victim.getHealth() : 0;
        double preAbs = victim != null ? victim.getAbsorptionAmount() : 0;

        double totalTrueDamage = trueDamage + trueTrueDamage;
        if (totalTrueDamage > 0 && victim != null && trueAttacker != null) {
            double left = Math.min(victim.getMaxHealth(), Math.max(0.0, victim.getHealth() - totalTrueDamage));
            if (left <= 0.0) {
                event.setCancelled(true);
                PlayerUtil.addHealth(victim, -victim.getMaxHealth() * 2);
            } else {
                victim.setHealth(left);
            }
        }

        if (victim != null && trueAttacker != null) {
            double totalDamageDealt = totalTrueDamage + event.getFinalDamage();
            String extraContent = extraMessageContent;
            int gold = extraGold;

            Bukkit.getScheduler().runTask(Pit.getInstance(), () -> {
                double postAbs = victim.getAbsorptionAmount();
                String indicator = getIndicator(originalHealth, (int) preAbs, (int) postAbs, totalDamageDealt);
                if (extraContent != null) {
                    indicator += extraContent;
                }
                if (gold > 0) {
                    indicator += " §6+" + gold + "g";
                }
                PlayerUtil.displayIndicator(trueAttacker, indicator);
            });
        }
    }

    private String getIndicator(int health, int preAbs, int postAbs, double totalDamage) {
        StringBuilder builder = new StringBuilder();

        builder.append(PlayerUtil.getRankColor(victim))
                .append(victim.getDisplayName())
                .append(" ");

        int max = (int) victim.getMaxHealth() >> 1;
        int dmg = (int) totalDamage >> 1;
        int current = health >> 1;

        int pre = preAbs >> 1;
        int post = postAbs >> 1;
        int absHit = pre - post;

        int healthHit = Math.max(0, dmg - absHit);
        int left = Math.max(0, current - healthHit);
        int empty = max - current;

        appendHearts(builder, ChatColor.DARK_RED, left);
        appendHearts(builder, ChatColor.RED, healthHit);
        appendHearts(builder, ChatColor.BLACK, empty);
        appendHearts(builder, ChatColor.YELLOW, post);
        appendHearts(builder, ChatColor.GOLD, absHit);

        return builder.toString();
    }

    private void appendHearts(StringBuilder builder, ChatColor color, int count) {
        if (count <= 0) return;
        builder.append(color).append("❤".repeat(Math.min(20, count)));
    }
}
