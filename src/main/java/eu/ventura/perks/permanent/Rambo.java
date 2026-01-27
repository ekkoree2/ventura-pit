package eu.ventura.perks.permanent;

import eu.ventura.constants.Strings;
import eu.ventura.enchantment.EnchantType;
import eu.ventura.event.PitKillEvent;
import eu.ventura.perks.Perk;
import eu.ventura.util.LoreBuilder;
import eu.ventura.util.PlayerUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * author: ekkoree
 * created at: 1/26/2026
 */
public class Rambo extends Perk {
    @Override
    public String getId() {
        return "rambo";
    }

    @Override
    public String getDisplayName(Strings.Language language) {
        return Strings.Simple.PERK_RAMBO.get(language);
    }

    @Override
    public int getLevel() {
        return 70;
    }

    @Override
    public int getGold() {
        return 6000;
    }

    @Override
    public int getRenownCost() {
        return 15;
    }

    @Override
    public int getPrestigeRequirement() {
        return 3;
    }

    @Override
    public EnchantType getType() {
        return EnchantType.DEFENSIVE;
    }

    @Override
    public LoreBuilder getDescription(Strings.Language language) {
        return Strings.Lore.RAMBO_DESC.get(language);
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.STICK);
    }

    @Override
    public boolean isHealing() {
        return true;
    }

    @Override
    public void onEquip(Player player) {
        PlayerUtil.updateMaxHealth(player, false);
    }

    @Override
    public void onUnequip(Player player) {
        PlayerUtil.updateMaxHealth(player, false);
    }

    @Override
    public void onKill(PitKillEvent event) {
        Player player = event.data.trueAttacker;
        if (player == null) {
            return;
        }
        player.setHealth(player.getMaxHealth());
    }
}
