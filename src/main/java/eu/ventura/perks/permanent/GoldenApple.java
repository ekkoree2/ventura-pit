package eu.ventura.perks.permanent;

import eu.ventura.Pit;
import eu.ventura.constants.Strings;
import eu.ventura.enchantment.EnchantType;
import eu.ventura.perks.Perk;
import eu.ventura.util.LoreBuilder;
import eu.ventura.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * author: ekkoree
 * created at: 1/27/2026
 */
public class GoldenApple extends Perk {

    @Override
    public String getId() {
        return "golden-apple";
    }

    @Override
    public String getDisplayName(Strings.Language language) {
        return "Golden Apple";
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getGold() {
        return 0;
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
        return EnchantType.DEFENSIVE;
    }

    @Override
    public LoreBuilder getDescription(Strings.Language language) {
        return new LoreBuilder();
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.GOLDEN_APPLE);
    }

    @Override
    public boolean isHealing() {
        return true;
    }

    @Override
    public boolean isHidden() {
        return true;
    }

    public void applyEffects(Player player, double currentAbs) {
        Bukkit.getScheduler().runTask(Pit.instance, () -> {
            player.removePotionEffect(PotionEffectType.REGENERATION);
            player.removePotionEffect(PotionEffectType.ABSORPTION);
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 80, 2));

            double newAbs = Math.min(currentAbs + 6, 10);
            PlayerUtil.setAbs(player, newAbs);
        });
    }
}
