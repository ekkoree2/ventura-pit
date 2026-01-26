package eu.ventura.renown.impl.upgrades;

import eu.ventura.event.PitKillEvent;
import eu.ventura.model.PlayerModel;
import eu.ventura.renown.RenownCategory;
import eu.ventura.renown.RenownShop;
import eu.ventura.service.PlayerService;
import eu.ventura.util.LoreBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * author: ekkoree
 * created at: 1/24/2026
 */
public class Tenacity extends RenownShop {
    public Tenacity() {
        super(RenownCategory.UPGRADES, Material.MAGMA_CREAM,
                repeatTiers(getLore(),
                        t(1, 10, 1),
                        t(2, 50, 1)
                )
        );
    }

    private static List<String> getLore() {
        return new LoreBuilder()
                .add("§7Each tier:")
                .addNewline("§7Heal §c0.5❤§7 on kill.")
                .compile();
    }

    @Override
    public String getCurrentBoost(int tier) {
        return "§7Heal §c" + getHealAmount(tier) + "❤§7 on kill.";
    }

    private static double getHealAmount(int tier) {
        return 0.5 * Math.min(2, tier);
    }

    @Override
    public void onKill(PitKillEvent event, int tierLevel) {
        double health = getHealAmount(tierLevel) * 2;
        Player attacker = event.data.trueAttacker;
        if (attacker != null) {
            double newHealth = Math.min(attacker.getMaxHealth(), attacker.getHealth() + health);
            attacker.setHealth(newHealth);
        }
    }
}
