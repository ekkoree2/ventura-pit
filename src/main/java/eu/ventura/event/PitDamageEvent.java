package eu.ventura.event;

import eu.ventura.model.AttackModel;
import eu.ventura.model.DeathModel;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PitDamageEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    public final AttackModel attackModel;
    public final DeathModel data;
    private final double finalDamage;

    public PitDamageEvent(AttackModel attackModel, DeathModel data, double finalDamage) {
        this.attackModel = attackModel;
        this.data = data;
        this.finalDamage = finalDamage;
    }

    public Player getVictim() {
        return data.victim;
    }

    public Player getTrueAttacker() {
        return data.trueAttacker;
    }

    public double getFinalDamage() {
        return finalDamage;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
