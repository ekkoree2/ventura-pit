package eu.ventura.enchantment.sword;

import eu.ventura.constants.Sounds;
import eu.ventura.constants.Strings;
import eu.ventura.enchantment.*;
import eu.ventura.model.AttackModel;
import eu.ventura.model.PlayerModel;
import eu.ventura.util.LoreBuilder;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

/**
 * author: ekkoree
 * created at: 1/15/2025
 */
public class Crater implements PitEnchant {
    private int getDamage(int level) {
        return 1 << (level - 1);
    }

    @Override
    public String getDisplayName() {
        return "Crater";
    }

    @Override
    public LoreBuilder getDescription(int level, Strings.Language language) {
        int damage = getDamage(level);
        return new LoreBuilder()
                .setMaxLength(26)
                .add(Strings.Formatted.CRATER.format(language, damage));
    }

    @Override
    public EnchantRarity getRarity() {
        return EnchantRarity.REGULAR;
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
    public int getPriority() {
        return 2;
    }

    @Override
    public void apply(int level, AttackModel model) {
        if (!model.isFalling(model.trueAttacker)) {
            return;
        }

        if (!PlayerModel.getInstance(model.trueAttacker).effectsModel.canCooldown(getId(), 500)) {
            return;
        }

        int blocks = (int) model.trueAttacker.getFallDistance();
        model.damageMultiplier += blocks * (getDamage(level) / 100.0);
        Sounds.CRATER.play(model.trueAttacker.getLocation());

        if (level > 1) {
            model.trueAttacker.setVelocity(new Vector(0, 1.5, 0));
            model.trueAttacker.spawnParticle(
                    Particle.EXPLOSION,
                    model.victim.getLocation().add(0, 2, 0),
                    1
            );
        }
    }
}
