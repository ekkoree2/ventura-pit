package eu.ventura.model.game.impl.renown.impl;

import eu.ventura.constants.Sounds;
import eu.ventura.constants.Strings;
import eu.ventura.menu.renown.RenownPerksGUI;
import eu.ventura.model.PlayerModel;
import eu.ventura.model.RenownTierModel;
import eu.ventura.model.TriggerModel;
import eu.ventura.model.game.impl.renown.RenownUpgradeModel;
import eu.ventura.perks.Perk;
import eu.ventura.renown.RenownShop;
import eu.ventura.util.LevelUtil;
import eu.ventura.util.MathUtil;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * author: ekkoree
 * created at: 1/24/2026
 */
public class RenownPerksModel extends RenownUpgradeModel {
    public RenownPerksModel(PlayerModel playerModel, RenownShop upgrade) {
        super(playerModel, upgrade, new RenownPerksGUI(playerModel.player), "Perk");
    }

    @Override
    public Runnable getChildTask(Integer slot) {
        return () -> {
            Strings.Language lang = playerModel.language;
            int cost = upgrade.getTier(currentTier + 1).getRenown();
            playerModel.setRenown(playerModel.getRenown() - cost);
            playerModel.setRenownPerkTier(upgrade, currentTier + 1);

            Perk perk = upgrade.getPerkInstance();
            String message = perk != null ? perk.getDisplayName(lang) : upgrade.getDisplayName();
            Sounds.ITEM_PURCHASE.play(player);
            player.sendMessage(Strings.Formatted.RENOWN_UNLOCK_MSG.format(lang, message));
            gui.open();
        };
    }

    @Override
    public TriggerModel getTrigger() {
        Strings.Language lang = playerModel.language;
        RenownTierModel nextTier = upgrade.getTier(currentTier + 1);
        if (nextTier == null) {
            return new TriggerModel(
                    Sounds.NO,
                    Strings.Simple.RENOWN_ALREADY_UNLOCKED_PERK.get(lang),
                    TriggerModel.Mode.PASS
            );
        }
        if (playerModel.getPrestige() < nextTier.getPrestige()) {
            return new TriggerModel(
                    Sounds.NO,
                    Strings.Simple.RENOWN_PRESTIGE_TOO_LOW.get(lang),
                    TriggerModel.Mode.PASS
            );
        }
        if (playerModel.getRenown() < nextTier.getRenown()) {
            return new TriggerModel(
                    Sounds.NO,
                    Strings.Simple.RENOWN_NOT_ENOUGH.get(lang),
                    TriggerModel.Mode.PASS
            );
        }
        return new TriggerModel(null, null, TriggerModel.Mode.CONFIRM_PANEL);
    }

    @Override
    protected void getExtraInfo(List<String> lore) {
        Strings.Language lang = playerModel.language;
        RenownTierModel nextTier = upgrade.getTier(currentTier + 1);
        Perk perk = upgrade.getPerkInstance();

        String formatted = LevelUtil.getFormattedLevelFromValues(playerModel.getPrestige(), perk.getLevel());

        List<String> fixed = new ArrayList<>(Arrays.asList(
                Strings.Formatted.RENOWN_LEVEL.format(lang, formatted),
                Strings.Formatted.RENOWN_GOLD_COST.format(lang, NumberFormat.getInstance().format((int) perk.getGold())),
                "",
                "§e" + perk.getDisplayName(lang)
        ));
        fixed.addAll(perk.getDescription(lang).compile());
        fixed.add("");
        fixed.addAll(Arrays.asList(
                Strings.Simple.RENOWN_PERK_UNLOCK_1.get(lang),
                Strings.Simple.RENOWN_PERK_UNLOCK_2.get(lang),
                Strings.Simple.RENOWN_PERK_UNLOCK_3.get(lang),
                Strings.Simple.RENOWN_PERK_UNLOCK_4.get(lang),
                ""
        ));

        if (nextTier == null) {
            fixed.add(Strings.Simple.RENOWN_UNLOCKED.get(lang));
        } else {
            if (nextTier.getPrestige() > playerModel.getPrestige()) {
                fixed.add(Strings.Formatted.RENOWN_PRESTIGE_DISPLAY.format(lang, MathUtil.roman(nextTier.getPrestige())));
            }
        }

        lore.addAll(0, fixed);
    }
}
