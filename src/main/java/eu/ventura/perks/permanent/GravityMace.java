package eu.ventura.perks.permanent;

import eu.ventura.constants.Strings;
import eu.ventura.enchantment.EnchantType;
import eu.ventura.java.NewString;
import eu.ventura.model.AttackModel;
import eu.ventura.perks.Perk;
import eu.ventura.util.ItemHelper;
import eu.ventura.util.LoreBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * author: ekkoree
 * created at: 1/15/2026
 */
public class GravityMace extends Perk {
    @Override
    public String getId() {
        return "mace";
    }

    @Override
    public String getDisplayName() {
        return "Gravity Mace";
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
    public ItemStack getIcon() {
        return new ItemStack(Material.MACE);
    }

    @Override
    public int getLevel() {
        return 20;
    }

    @Override
    public int getGold() {
        return 3000;
    }

    @Override
    public LoreBuilder getDescription(Strings.Language language) {
        return new LoreBuilder()
                .add(Strings.Simple.GRAVITY_MACE.get(language));
    }

    @Override
    public void apply(AttackModel model) {
        if (model.isFalling(model.trueAttacker)) {
            model.damageMultiplier += 0.1;
            model.extraMessageContent += NewString.of(" &a&lMACE!");
        }
    }
}
