package eu.ventura.model.game.impl.renown.impl;

import eu.ventura.menu.renown.RenownUpgradesGUI;
import eu.ventura.model.PlayerModel;
import eu.ventura.model.RenownTierModel;
import eu.ventura.model.game.impl.renown.RenownUpgradeModel;
import eu.ventura.renown.RenownShop;
import eu.ventura.util.MathUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * author: ekkoree
 * created at: 1/24/2026
 */
public class RenownUpgradesModel extends RenownUpgradeModel {
    public RenownUpgradesModel(PlayerModel playerModel, RenownShop upgrade) {
        super(playerModel, upgrade, new RenownUpgradesGUI(playerModel.player), null);
    }

    @Override
    protected void getExtraInfo(List<String> lore) {
        RenownTierModel nextTier = upgrade.getTier(currentTier + 1);
        List<String> extra = new ArrayList<>();

        if (currentTier > 0 && upgrade.getTiers().size() > 1) {
            String boost = upgrade.getCurrentBoost(currentTier);
            if (boost != null) {
                extra.add("§7Current: " + boost);
            }
            extra.add("§7Tier: §a" + MathUtil.roman(currentTier));
            extra.add("");
        }

        if (nextTier == null) {
            extra.addAll(upgrade.getMaxedTierInfo(currentTier));
            extra.add("");
        } else {
            extra.addAll(nextTier.getLore());
            extra.add("");
        }
        lore.addAll(0, extra);
    }
}
