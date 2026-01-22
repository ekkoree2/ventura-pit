package eu.ventura.perks.permanent;

import eu.ventura.Pit;
import eu.ventura.constants.Strings;
import eu.ventura.enchantment.EnchantType;
import eu.ventura.event.PitKillEvent;
import eu.ventura.events.major.impl.RagePit;
import eu.ventura.model.AttackModel;
import eu.ventura.perks.Perk;
import eu.ventura.util.LoreBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * author: ekkoree
 * created at: 1/19/2026
 */
public class Vampire extends Perk {
    @Override
    public String getId() {
        return "vampire";
    }

    @Override
    public String getDisplayName(Strings.Language language) {
        return Strings.Simple.PERK_VAMPIRE.get(language);
    }

    @Override
    public int getLevel() {
        return 60;
    }

    @Override
    public int getGold() {
        return 4000;
    }

    @Override
    public int getRenownCost() {
        return 0;
    }

    @Override
    public int getPrestigeRequirement() {
        return 0;
    }

    @Override
    public EnchantType getType() {
        return EnchantType.OFFENSIVE;
    }

    @Override
    public LoreBuilder getDescription(Strings.Language language) {
        return Strings.Lore.VAMPIRE_DESC.get(language);
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.FERMENTED_SPIDER_EYE);
    }

    @Override
    public boolean isHealing() {
        return true;
    }

    @Override
    public void apply(AttackModel model) {
        if (Pit.event instanceof RagePit) {
            return;
        }

        if (model.trueAttacker == null) {
            return;
        }

        if (model.attacker instanceof Arrow arrow) {
            if (arrow.isCritical()) {
                model.attackerHealthGained += 4.0;
            } else {
                model.attackerHealthGained += 1;
            }
        } else if (model.attacker instanceof Player) {
            model.attackerHealthGained += 2.0;
        }
    }

    @Override
    public void onKill(PitKillEvent event) {
        if (Pit.event instanceof RagePit) {
            return;
        }

        Player player = event.data.trueAttacker;
        if (player == null) {
            return;
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 160, 0));
    }
}
