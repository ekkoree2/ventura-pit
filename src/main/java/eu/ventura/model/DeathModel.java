package eu.ventura.model;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * author: ekkoree
 * created at: 12/26/2025
 */
public class DeathModel {
    public final Player trueAttacker;
    public final Entity entity;
    public final Player victim;
    public final EntityDamageEvent event;
    public Integer xp = 5;
    public Integer xpBonus = 0;
    public Double gold = 10d;
    public Double xpMultiplier = 1.0;
    public Double goldMultiplier = 1.0;

    public DeathModel(Player trueAttacker, Entity entity, Player victim, EntityDamageEvent event) {
        this.trueAttacker = trueAttacker;
        this.entity = entity;
        this.victim = victim;
        this.event = event;
    }

    public DeathModel(Player victim, EntityDamageEvent event) {
        this(null, null, victim, event);
    }

    public DeathModel(Player victim) {
        this(null, null, victim, null);
    }

    public int getFinalXp() {
        return (int) Math.min(400, (xp + xpBonus) * xpMultiplier);
    }

    public double getFinalGold() {
        return gold * goldMultiplier;
    }
}
