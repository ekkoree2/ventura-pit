package eu.ventura.model.game.impl.renown.impl;

import eu.ventura.constants.Strings;
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
        Strings.Language lang = playerModel.language;
        RenownTierModel nextTier = upgrade.getTier(currentTier + 1);
        List<String> extra = new ArrayList<>();

        if (currentTier > 0 && upgrade.getTiers().size() > 1) {
            String boost = upgrade.getCurrentBoost(currentTier, lang);
            if (boost != null) {
                extra.add(Strings.Formatted.RENOWN_CURRENT.format(lang, boost));
            }
            extra.add(Strings.Formatted.RENOWN_TIER.format(lang, MathUtil.roman(currentTier)));
            extra.add("");
        }

        if (nextTier == null) {
            extra.addAll(upgrade.getMaxedTierInfo(currentTier, lang));
            extra.add("");
        } else {
            extra.addAll(nextTier.getLore(lang));
            extra.add("");
        }
        lore.addAll(0, extra);
    }
}
