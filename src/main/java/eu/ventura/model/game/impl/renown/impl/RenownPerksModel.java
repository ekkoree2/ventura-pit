package eu.ventura.model.game.impl.renown.impl;

import eu.ventura.constants.Sounds;
import eu.ventura.menu.renown.RenownPerksGUI;
import eu.ventura.model.PlayerModel;
import eu.ventura.model.RenownTierModel;
import eu.ventura.model.TriggerModel;
import eu.ventura.model.game.impl.renown.RenownUpgradeModel;
import eu.ventura.perks.Perk;
import eu.ventura.renown.RenownShop;
import eu.ventura.util.MathUtil;

import java.util.ArrayList;
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
            int cost = upgrade.getTier(currentTier + 1).getRenown();
            playerModel.setRenown(playerModel.getRenown() - cost);
            playerModel.setRenownPerkTier(upgrade, currentTier + 1);

            Perk perk = upgrade.getPerkInstance();
            String message = perk != null ? perk.getDisplayName(playerModel.language) : upgrade.getDisplayName();
            Sounds.ITEM_PURCHASE.play(player);
            player.sendMessage("§a§lUNLOCK!§6 " + message);
            gui.open();
        };
    }

    @Override
    public TriggerModel getTrigger() {
        RenownTierModel nextTier = upgrade.getTier(currentTier + 1);
        if (nextTier == null) {
            return new TriggerModel(
                    Sounds.NO,
                    "§aYou already unlocked this perk!",
                    TriggerModel.Mode.PASS
            );
        }
        if (playerModel.getPrestige() < nextTier.getPrestige()) {
            return new TriggerModel(
                    Sounds.NO,
                    "§cYou are too low prestige to acquire this!",
                    TriggerModel.Mode.PASS
            );
        }
        if (playerModel.getRenown() < nextTier.getRenown()) {
            return new TriggerModel(
                    Sounds.NO,
                    "§cYou don't have enough renown to afford this!",
                    TriggerModel.Mode.PASS
            );
        }
        return new TriggerModel(null, null, TriggerModel.Mode.CONFIRM_PANEL);
    }

    @Override
    protected void getExtraInfo(List<String> lore) {
        RenownTierModel nextTier = upgrade.getTier(currentTier + 1);
        List<String> extra = new ArrayList<>();

        Perk perk = upgrade.getPerkInstance();
        if (perk != null) {
            extra.addAll(perk.getDescription(playerModel.language).compile());
            extra.add("");
            extra.add("§7Level: §a" + perk.getLevel());
            extra.add("§7Gold cost: §6" + (int) perk.getGold() + "g");
            extra.add("");
        }

        if (nextTier == null) {
            extra.add("§aUnlocked!");
        } else {
            if (nextTier.getPrestige() > playerModel.getPrestige()) {
                extra.add("§7Prestige: §e" + MathUtil.roman(nextTier.getPrestige()));
            }
        }

        lore.addAll(0, extra);
    }
}
