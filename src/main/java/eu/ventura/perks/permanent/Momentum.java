package eu.ventura.perks.permanent;

import eu.ventura.Pit;
import eu.ventura.constants.PitEvent;
import eu.ventura.constants.Strings;
import eu.ventura.enchantment.EnchantType;
import eu.ventura.event.PitKillEvent;
import eu.ventura.events.major.impl.RagePit;
import eu.ventura.model.AttackModel;
import eu.ventura.perks.Perk;
import eu.ventura.util.LoreBuilder;
import eu.ventura.util.PlayerUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * author: ekkoree
 * created at: 1/26/2026
 */
public class Momentum extends Perk {
    @Override
    public String getId() {
        return "momentum";
    }

    @Override
    public String getDisplayName(Strings.Language language) {
        return "Momentum";
    }

    @Override
    public int getLevel() {
        return 25;
    }

    @Override
    public int getGold() {
        return 2500;
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
        return Strings.Lore.MOMENTUM.get(language);
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.SUGAR);
    }

    @Override
    public boolean isHealing() {
        return true;
    }

    @Override
    public void onKill(PitKillEvent event) {
        if (Pit.event instanceof RagePit) {
            return;
        }
        PlayerUtil.heal(event.data.trueAttacker, 6);
        event.data.trueAttacker.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 160, 0));
    }
}
