package eu.ventura.perks.permanent;

import eu.ventura.constants.EffectSource;
import eu.ventura.constants.Strings;
import eu.ventura.enchantment.EnchantType;
import eu.ventura.model.AttackModel;
import eu.ventura.model.PlayerEffectsModel;
import eu.ventura.perks.Perk;
import eu.ventura.util.LoreBuilder;
import eu.ventura.util.PlayerUtil;
import eu.ventura.util.RegionHelper;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * author: ekkoree
 * created at: 1/19/2026
 */
public class Calculated extends Perk {
    @Override
    public String getId() {
        return "calculated";
    }

    @Override
    public String getDisplayName(Strings.Language language) {
        return Strings.Simple.PERK_CALCULATED.get(language);
    }

    @Override
    public int getLevel() {
        return 45;
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
        return Strings.Lore.CALCULATED_PERK.get(language);
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.GOLDEN_SWORD);
    }

    @Override
    public void apply(AttackModel model) {
        if (model.trueAttacker == null) {
            return;
        }

        PlayerEffectsModel effects = PlayerEffectsModel.get(model.trueAttacker);
        if (effects.isActive(EffectSource.PERK, "calculated")) {
            model.damageMultiplier += 0.28;
        }
    }

    @Override
    public void onSwing(PlayerAnimationEvent event) {
        Player player = event.getPlayer();
        PlayerEffectsModel effects = PlayerEffectsModel.get(player);
        double range = 3.2;

        if (RegionHelper.isInSpawn(player)) {
            return;
        }

        boolean hasNearbyPlayer = false;
        for (Entity entity : player.getNearbyEntities(range, range, range)) {
            if (!(entity instanceof Player target)) {
                continue;
            }

            hasNearbyPlayer = true;
            if (PlayerUtil.isLookingAt(player, target, range, 0.25)) {
                effects.setActive(EffectSource.PERK, "calculated", 1);
                return;
            }
        }

        if (hasNearbyPlayer) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20, 1));
        }
    }
}
