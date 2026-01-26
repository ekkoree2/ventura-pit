package eu.ventura.renown.impl.perks;

import eu.ventura.model.PlayerModel;
import eu.ventura.model.RenownTierModel;
import eu.ventura.perks.Perk;
import eu.ventura.renown.RenownCategory;
import eu.ventura.renown.RenownShop;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.function.Supplier;

/**
 * author: ekkoree
 * created at: 1/24/2026
 */
public class RenownPerk extends RenownShop {
    private final Supplier<Perk> supply;
    private final String id;
    private final String displayName;

    private RenownPerk(Supplier<Perk> supply, int renownCost, int requiredPrestige) {
        super(Collections.singletonList(
                new RenownTierModel(1, renownCost, requiredPrestige, null)
        ));
        this.supply = supply;
        Perk perk = supply.get();
        this.id = perk.getId();
        this.displayName = perk.getDisplayName(null);
    }

    public static RenownPerk of(Supplier<Perk> supply) {
        Perk instance = supply.get();
        return new RenownPerk(supply, instance.getRenownCost(), instance.getPrestigeRequirement());
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public RenownCategory getCategory() {
        return RenownCategory.PERKS;
    }

    @Override
    public ItemStack getIcon(PlayerModel playerModel) {
        return supply.get().getIcon();
    }

    @Override
    public Perk getPerkInstance() {
        return supply.get();
    }
}
