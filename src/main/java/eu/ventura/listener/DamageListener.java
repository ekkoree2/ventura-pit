package eu.ventura.listener;

import eu.ventura.constants.Strings;
import eu.ventura.enchantment.EnchantType;
import eu.ventura.event.PitAssistEvent;
import eu.ventura.event.PitDamageEvent;
import eu.ventura.event.PitDeathEvent;
import eu.ventura.event.PitKillEvent;
import eu.ventura.model.AttackEnchantModel;
import eu.ventura.model.AttackModel;
import eu.ventura.model.DeathModel;
import eu.ventura.model.PlayerModel;
import eu.ventura.perks.Perk;
import eu.ventura.service.PerkService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import eu.ventura.util.NBTHelper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

/**
 * author: ekkoree
 * created at: 1/15/2025
 */
@SuppressWarnings("unused")
public class DamageListener implements Listener {
    private final List<EntityDamageEvent.DamageCause> causes = List.of(
            EntityDamageEvent.DamageCause.WORLD_BORDER,
            EntityDamageEvent.DamageCause.FALL,
            EntityDamageEvent.DamageCause.WITHER,
            EntityDamageEvent.DamageCause.SUICIDE,
            EntityDamageEvent.DamageCause.CAMPFIRE
    );

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageEvent event) {
        if (causes.contains(event.getCause())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Player attacker = getAttacker(event.getDamager());
        if (attacker == null) {
            return;
        }
        if (!(event.getEntity() instanceof Player victim)) {
            return;
        }
        if (attacker.equals(victim)) {
            return;
        }
        if (!event.isCancelled()) {
            PlayerModel attackerModel = PlayerModel.getInstance(attacker);
            attackerModel.runCombatTime();
            attackerModel.updateStatus();
            attackerModel.lastAttacker = victim;

            PlayerModel victimModel = PlayerModel.getInstance(victim);
            victimModel.runCombatTime();
            victimModel.updateStatus();
            victimModel.lastAttacker = attacker;
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageHigh(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player victim)) {
            return;
        }

        Entity source = event.getDamager();
        Player trueAttacker = getAttacker(source);

        AttackModel attackModel = new AttackModel(event, victim, source);
        if (trueAttacker != null) {
            attackModel.trueAttacker = trueAttacker;
        }

        AttackEnchantModel enchantModel = new AttackEnchantModel(victim, source, trueAttacker);
        enchantModel.collectEnchantments();

        enchantModel.applyEnchantments((enchant, level) -> enchant.apply(level, attackModel), EnchantType.OFFENSIVE);

        if (!attackModel.cancelled) {
            event.setDamage(attackModel.getFinalDamage());
        } else {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player victim)) {
            return;
        }

        Entity source = event.getDamager();
        Player trueAttacker = getAttacker(source);

        if (trueAttacker != null && !event.isCancelled()) {
            AttackModel attackModel = new AttackModel(event, victim, source);
            attackModel.trueAttacker = trueAttacker;
            DeathModel deathModel = new DeathModel(trueAttacker, source, victim, event);
            Bukkit.getPluginManager().callEvent(new PitDamageEvent(attackModel, deathModel, event.getFinalDamage()));

            if (event.isCritical() && source instanceof Player) {
                attackModel.extraMessageContent += (" " + Strings.Simple.CRIT.get(trueAttacker));
            }

            attackModel.finalApply();
        }

        if (victim.getHealth() - event.getFinalDamage() <= 0.0) {
            DeathModel deathModel = new DeathModel(
                    trueAttacker,
                    source,
                    victim,
                    event
            );
            event.setCancelled(true);
            if (trueAttacker != null) {
                Bukkit.getPluginManager().callEvent(new PitKillEvent(deathModel));
            }
            Bukkit.getPluginManager().callEvent(new PitDeathEvent(deathModel));
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPitDamage(PitDamageEvent event) {
        Player attacker = event.getTrueAttacker();
        Player victim = event.getVictim();
        AttackModel attackModel = event.attackModel;

        if (attacker != null) {
            PlayerModel attackerModel = PlayerModel.getInstance(attacker);
            for (Map.Entry<Integer, String> entry : attackerModel.equippedPerks.entrySet()) {
                Perk perk = PerkService.getPerk(entry.getValue());
                if (perk != null && !perk.isCancelled() && (perk.getType() == eu.ventura.enchantment.EnchantType.OFFENSIVE || perk.getType() == eu.ventura.enchantment.EnchantType.BOTH)) {
                    perk.apply(attackModel);
                }
            }
        }

        if (victim != null) {
            PlayerModel victimModel = PlayerModel.getInstance(victim);
            for (Map.Entry<Integer, String> entry : victimModel.equippedPerks.entrySet()) {
                Perk perk = PerkService.getPerk(entry.getValue());
                if (perk != null && !perk.isCancelled() && (perk.getType() == eu.ventura.enchantment.EnchantType.DEFENSIVE || perk.getType() == eu.ventura.enchantment.EnchantType.BOTH)) {
                    perk.apply(attackModel);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPitKill(PitKillEvent event) {
        Player killer = event.data.trueAttacker;
        if (killer == null) {
            return;
        }

        PlayerModel killerModel = PlayerModel.getInstance(killer);
        for (Map.Entry<Integer, String> entry : killerModel.equippedPerks.entrySet()) {
            Perk perk = PerkService.getPerk(entry.getValue());
            if (perk != null && !perk.isCancelled()) {
                perk.onKill(event);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPitDeath(PitDeathEvent event) {
        Player victim = event.data.victim;
        if (victim == null) {
            return;
        }

        PlayerModel victimModel = PlayerModel.getInstance(victim);
        for (Map.Entry<Integer, String> entry : victimModel.equippedPerks.entrySet()) {
            Perk perk = PerkService.getPerk(entry.getValue());
            if (perk != null && !perk.isCancelled()) {
                perk.onDeath(event);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPitAssist(PitAssistEvent event) {
        Player assister = event.assister;
        if (assister == null) {
            return;
        }

        PlayerModel assisterModel = PlayerModel.getInstance(assister);
        for (Map.Entry<Integer, String> entry : assisterModel.equippedPerks.entrySet()) {
            Perk perk = PerkService.getPerk(entry.getValue());
            if (perk != null && !perk.isCancelled()) {
                perk.onAssist(event);
            }
        }
    }

    @EventHandler
    public void onPerkInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        String perkId = NBTHelper.getString(item, "pit-perk-item");
        if (perkId == null) {
            return;
        }

        Perk perk = PerkService.getPerk(perkId);
        if (perk != null) {
            perk.onInteract(event);
        }
    }

    @EventHandler
    public void onPerkSwing(PlayerAnimationEvent event) {
        if (event.getAnimationType() != PlayerAnimationType.ARM_SWING) {
            return;
        }

        Player player = event.getPlayer();
        PlayerModel playerModel = PlayerModel.getInstance(player);

        for (Map.Entry<Integer, String> entry : playerModel.equippedPerks.entrySet()) {
            Perk perk = PerkService.getPerk(entry.getValue());
            if (perk != null && !perk.isCancelled()) {
                perk.onSwing(event);
            }
        }
    }

    private Player getAttacker(Entity damager) {
        if (damager instanceof Player) {
            return (Player) damager;
        }
        if (damager instanceof Projectile projectile) {
            if (projectile.getShooter() instanceof Player) {
                return (Player) projectile.getShooter();
            }
        }
        return null;
    }
}
