package eu.ventura.enchantment.sword;

import eu.ventura.constants.Strings;
import eu.ventura.enchantment.*;
import eu.ventura.model.AttackModel;
import eu.ventura.util.LoreBuilder;

/**
 * author: ekkoree
 * created at: 1/26/2026
 */
public class Sharp implements PitEnchant {
    private int getDamage(int level) {
        if (level == 1) {
            return 5;
        }
        return 5 + ((level - 1) * 6);
    }

    @Override
    public String getDisplayName() {
        return "Sharp";
    }

    @Override
    public LoreBuilder getDescription(int level, Strings.Language language) {
        int dmg = getDamage(level);
        return new LoreBuilder().add(Strings.Formatted.SHARP_ENCH.format(language, dmg));
    }

    @Override
    public EnchantRarity getRarity() {
        return EnchantRarity.LAME;
    }

    @Override
    public EnchantItem[] getItemTypes() {
        return new EnchantItem[]{EnchantItem.SWORD};
    }

    @Override
    public EnchantType getType() {
        return EnchantType.OFFENSIVE;
    }

    @Override
    public ApplyOn getApplyOn() {
        return ApplyOn.WEAPON;
    }

    @Override
    public void apply(int level, AttackModel model) {
        model.damageMultiplier += (getDamage(level) / 100.0);
    }
}
