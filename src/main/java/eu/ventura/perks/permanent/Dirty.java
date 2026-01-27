package eu.ventura.perks.permanent;

import eu.ventura.constants.Strings;
import eu.ventura.enchantment.EnchantType;
import eu.ventura.event.PitKillEvent;
import eu.ventura.perks.Perk;
import eu.ventura.util.LoreBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * author: ekkoree
 * created at: 1/26/2026
 */
public class Dirty extends Perk {
    @Override
    public String getId() {
        return "dirty";
    }

    @Override
    public String getDisplayName(Strings.Language language) {
        return Strings.Simple.PERK_DIRTY.get(language);
    }

    @Override
    public int getLevel() {
        return 80;
    }

    @Override
    public int getGold() {
        return 8000;
    }

    @Override
    public int getRenownCost() {
        return 15;
    }

    @Override
    public int getPrestigeRequirement() {
        return 2;
    }

    @Override
    public EnchantType getType() {
        return EnchantType.DEFENSIVE;
    }

    @Override
    public LoreBuilder getDescription(Strings.Language language) {
        return Strings.Lore.DIRTY_DESC.get(language);
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.PODZOL);
    }

    @Override
    public void onKill(PitKillEvent event) {
        Player player = event.data.trueAttacker;
        if (player == null) {
            return;
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 80, 1));
    }
}
