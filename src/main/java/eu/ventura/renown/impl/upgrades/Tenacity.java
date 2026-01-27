package eu.ventura.renown.impl.upgrades;

import eu.ventura.constants.Strings;
import eu.ventura.event.PitKillEvent;
import eu.ventura.renown.RenownCategory;
import eu.ventura.renown.RenownShop;
import eu.ventura.util.PlayerUtil;
import org.bukkit.Material;

import java.util.List;

/**
 * author: ekkoree
 * created at: 1/24/2026
 */
public class Tenacity extends RenownShop {
    public Tenacity() {
        super(RenownCategory.UPGRADES, Material.MAGMA_CREAM,
                repeatTiers(Strings.Lore.TENACITY.compile(Strings.Language.POLISH),
                        t(1, 10, 1),
                        t(2, 50, 1)
                )
        );
    }

    @Override
    public String getCurrentBoost(int tier, Strings.Language lang) {
        double healAmount = getHealAmount(tier);
        return lang == Strings.Language.POLISH
                ? "§7Ulecz §c" + healAmount + "❤§7 po zabójstwie."
                : "§7Heal §c" + healAmount + "❤§7 on kill.";
    }

    @Override
    public List<String> getMaxedTierInfo(int tier, Strings.Language lang) {
        return Strings.Lore.TENACITY.compile(lang);
    }

    private static double getHealAmount(int tier) {
        return 0.5 * Math.min(2, tier);
    }

    @Override
    public void onKill(PitKillEvent event, int tierLevel) {
        PlayerUtil.heal(event.data.trueAttacker, tierLevel);
    }
}
