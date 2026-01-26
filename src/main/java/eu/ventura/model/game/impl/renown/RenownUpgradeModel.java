package eu.ventura.model.game.impl.renown;

import dev.kyro.arcticapi.gui.AGUI;
import eu.ventura.constants.Sounds;
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
            int cost = upgrade.getTier(currentTier + 1).getRenown();
            playerModel.setRenown(playerModel.getRenown() - cost);
            playerModel.setRenownPerkTier(upgrade, currentTier + 1);

            String message = upgrade.getDisplayName();
            Sounds.ITEM_PURCHASE.play(player);
            player.sendMessage("§a§lPURCHASE!§6 " + message);
            gui.open();
        };
    }

    @Override
    public TriggerModel getTrigger() {
        RenownTierModel nextTier = upgrade.getTier(currentTier + 1);
        if (nextTier == null) {
            String message = upgrade.getTiers().size() > 1 ? "§aYou already unlocked last upgrade!" : "§aYou already unlocked this upgrade!";
            return new TriggerModel(
                    Sounds.NO,
                    message,
                    TriggerModel.Mode.PASS
            );
        }
        if (playerModel.getPrestige() < upgrade.getTier(1).getPrestige() || playerModel.getPrestige() < nextTier.getPrestige()) {
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
    public String getDisplayName() {
        RenownTierModel firstTier = upgrade.getTier(1);
        RenownTierModel nextTier = upgrade.getTier(currentTier + 1);

        if (firstTier == null) {
            return "§a" + (unlockName == null ? "" : unlockName + ": ") + upgrade.getDisplayName();
        }

        if (firstTier.getPrestige() > playerModel.getPrestige()) {
            return "§cUnknown upgrade";
        }

        boolean hasRenown = nextTier == null || playerModel.getRenown() >= nextTier.getRenown();
        boolean hasPrestige = nextTier == null || playerModel.getPrestige() >= nextTier.getPrestige();
        String color = !hasRenown || !hasPrestige ? "§c" : (currentTier == 0 ? "§e" : "§a");

        String namePart = unlockName == null ? "" : unlockName + ": ";
        return color + namePart + upgrade.getDisplayName();
    }

    @Override
    public List<String> getLore() {
        List<String> lore = new ArrayList<>();
        RenownTierModel tierLow = upgrade.getTier(1);
        RenownTierModel nextTier = upgrade.getTier(currentTier + 1);

        if (tierLow.getPrestige() > playerModel.getPrestige()) {
            return Collections.singletonList("§7Prestige: §e" + MathUtil.roman(tierLow.getPrestige()));
        }

        if (nextTier == null) {
            String message = upgrade.getTiers().size() > 1 ? "§aMax tier unlocked!" : "§aUnlocked!";
            lore.add(message);
        } else if (nextTier.getPrestige() > playerModel.getPrestige()) {
            lore.add("");
            lore.add("§7Required prestige: §e" + MathUtil.roman(nextTier.getPrestige()));
            lore.add("§cToo low prestige!");
        } else {
            lore.add("§7Cost: §e" + nextTier.getRenown() + " Renown");
            lore.add("§7You have: §e" + playerModel.getRenown() + " Renown");
            lore.add("");

            if (nextTier.getRenown() > playerModel.getRenown()) {
                lore.add("§cNot enough renown!");
            } else {
                lore.add("§eClick to purchase!");
            }
        }

        getExtraInfo(lore);
        return lore;
    }
}
