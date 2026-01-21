package eu.ventura.perks.permanent;

import eu.ventura.constants.Strings;
import eu.ventura.enchantment.EnchantType;
import eu.ventura.event.PitKillEvent;
import eu.ventura.perks.Perk;
import eu.ventura.util.LoreBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * author: ekkoree
 * created at: 1/20/2026
 */
public class Streaker extends Perk {
    @Override
    public String getId() {
        return "streaker";
    }

    @Override
    public String getDisplayName(Strings.Language language) {
        return Strings.Simple.STREAKER.get(language);
    }

    @Override
    public int getLevel() {
        return 50;
    }

    @Override
    public int getGold() {
        return 5000;
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
        return Strings.Lore.STREAKER_DESC.get(language);
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.WHEAT);
    }

    @Override
    public void onKill(PitKillEvent event) {
        event.data.xpBonus *= 3;
    }
}