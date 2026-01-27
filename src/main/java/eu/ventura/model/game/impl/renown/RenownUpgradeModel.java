package eu.ventura.model.game.impl.renown;

import dev.kyro.arcticapi.gui.AGUI;
import eu.ventura.constants.Sounds;
import eu.ventura.constants.Strings;
import eu.ventura.model.PlayerModel;
import eu.ventura.model.RenownTierModel;
import eu.ventura.model.TriggerModel;
import eu.ventura.model.game.GameModel;
import eu.ventura.renown.RenownShop;
import eu.ventura.util.MathUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * author: ekkoree
 * created at: 1/24/2026
 */
public abstract class RenownUpgradeModel extends GameModel {
    protected final RenownShop upgrade;
    protected final AGUI gui;
    protected final String unlockName;
    protected int currentTier;

    public RenownUpgradeModel(PlayerModel playerModel, RenownShop upgrade, AGUI gui, String unlockName) {
        super(playerModel);
        this.upgrade = upgrade;
        this.gui = gui;
        this.unlockName = unlockName;
        this.currentTier = playerModel.getRenownPerkTier(upgrade);
    }

    protected abstract void getExtraInfo(List<String> lore);

    @Override
    public Runnable getChildTask(Integer slot) {
        return () -> {
            Strings.Language lang = playerModel.language;
            int cost = upgrade.getTier(currentTier + 1).getRenown();
            playerModel.setRenown(playerModel.getRenown() - cost);
            playerModel.setRenownPerkTier(upgrade, currentTier + 1);

            String message = upgrade.getDisplayName();
            Sounds.ITEM_PURCHASE.play(player);
            player.sendMessage(Strings.Formatted.RENOWN_PURCHASE_MSG.format(lang, message));
            gui.open();
        };
    }

    @Override
    public TriggerModel getTrigger() {
        Strings.Language lang = playerModel.language;
        RenownTierModel nextTier = upgrade.getTier(currentTier + 1);
        if (nextTier == null) {
            String message = upgrade.getTiers().size() > 1
                    ? Strings.Simple.RENOWN_ALREADY_UNLOCKED_LAST.get(lang)
                    : Strings.Simple.RENOWN_ALREADY_UNLOCKED.get(lang);
            return new TriggerModel(
                    Sounds.NO,
                    message,
                    TriggerModel.Mode.PASS
            );
        }
        if (playerModel.getPrestige() < upgrade.getTier(1).getPrestige() || playerModel.getPrestige() < nextTier.getPrestige()) {
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
    public String getDisplayName() {
        Strings.Language lang = playerModel.language;
        RenownTierModel firstTier = upgrade.getTier(1);
        RenownTierModel nextTier = upgrade.getTier(currentTier + 1);

        if (firstTier == null) {
            return "§a" + (unlockName == null ? "" : unlockName + ": ") + upgrade.getDisplayName();
        }

        if (firstTier.getPrestige() > playerModel.getPrestige()) {
            return Strings.Simple.RENOWN_UNKNOWN_UPGRADE.get(lang);
        }

        boolean hasRenown = nextTier == null || playerModel.getRenown() >= nextTier.getRenown();
        boolean hasPrestige = nextTier == null || playerModel.getPrestige() >= nextTier.getPrestige();
        String color = !hasRenown || !hasPrestige ? "§c" : (currentTier == 0 ? "§e" : "§a");

        String namePart = unlockName == null ? "" : unlockName + ": ";
        return color + namePart + upgrade.getDisplayName();
    }

    @Override
    public List<String> getLore() {
        Strings.Language lang = playerModel.language;
        List<String> lore = new ArrayList<>();
        RenownTierModel tierLow = upgrade.getTier(1);
        RenownTierModel nextTier = upgrade.getTier(currentTier + 1);

        if (tierLow.getPrestige() > playerModel.getPrestige()) {
            return Collections.singletonList(Strings.Formatted.RENOWN_PRESTIGE_DISPLAY.format(lang, MathUtil.roman(tierLow.getPrestige())));
        }

        if (nextTier == null) {
            String message = upgrade.getTiers().size() > 1
                    ? Strings.Simple.RENOWN_MAX_TIER_UNLOCKED.get(lang)
                    : Strings.Simple.RENOWN_UNLOCKED.get(lang);
            lore.add(message);
        } else if (nextTier.getPrestige() > playerModel.getPrestige()) {
            lore.add("");
            lore.add(Strings.Formatted.RENOWN_REQUIRED_PRESTIGE.format(lang, MathUtil.roman(nextTier.getPrestige())));
            lore.add(Strings.Simple.RENOWN_TOO_LOW_PRESTIGE.get(lang));
        } else {
            lore.add(Strings.Formatted.RENOWN_COST.format(lang, nextTier.getRenown()));
            lore.add(Strings.Formatted.RENOWN_YOU_HAVE.format(lang, playerModel.getRenown()));
            lore.add("");

            if (nextTier.getRenown() > playerModel.getRenown()) {
                lore.add(Strings.Simple.RENOWN_NOT_ENOUGH_SHORT.get(lang));
            } else {
                lore.add(Strings.Simple.RENOWN_CLICK_TO_PURCHASE.get(lang));
            }
        }

        getExtraInfo(lore);
        return lore;
    }
}
