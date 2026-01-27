package eu.ventura.menu.renown.panel;

import dev.kyro.arcticapi.gui.AGUI;
import dev.kyro.arcticapi.gui.AGUIPanel;
import eu.ventura.constants.Strings;
import eu.ventura.menu.PrestigeGUI;
import eu.ventura.menu.renown.RenownPerksGUI;
import eu.ventura.menu.renown.RenownUpgradesGUI;
import eu.ventura.model.PlayerModel;
import eu.ventura.model.RenownIconModel;
import eu.ventura.model.RenownTierModel;
import eu.ventura.renown.RenownCategory;
import eu.ventura.renown.RenownShop;
import eu.ventura.service.PlayerService;
import eu.ventura.service.RenownService;
import eu.ventura.util.ItemHelper;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * author: ekkoree
 * created at: 1/24/2026
 */
public class MainRenownPanel extends AGUIPanel {
    private PlayerModel playerModel;

    public MainRenownPanel(AGUI gui) {
        super(gui);
        this.playerModel = PlayerService.getPlayer(player);
    }

    @Override
    public String getName() {
        return Strings.Simple.RENOWN_SHOP_TITLE.get(player);
    }

    @Override
    public int getRows() {
        return 3;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);

        if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.ARROW) {
            new PrestigeGUI(player).open();
            return;
        }

        Set<RenownIconModel> icons = RenownIconModel.getIcons();

        for (RenownIconModel icon : icons) {
            if (event.getSlot() == icon.getSlot()) {
                AGUI agui = icon.createGUI(player);
                if (agui != null) {
                    agui.open();
                }
                break;
            }
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        Strings.Language lang = playerModel.language;
        RenownIconModel.clearIcons();
        getInventory().setItem((getRows() - 1) * 9 + 4, ItemHelper.getReturnMenu(player, Strings.Simple.PRESTIGE_MENU_TITLE.get(lang)));

        RenownIconModel upgrades = RenownIconModel.create(
                Material.EMERALD,
                Strings.Simple.RENOWN_UPGRADES_ICON.get(lang),
                getUpgradesLore(Arrays.asList(
                        Strings.Simple.RENOWN_UPGRADES_DESC_1.get(lang),
                        Strings.Simple.RENOWN_UPGRADES_DESC_2.get(lang)
                ), RenownCategory.UPGRADES),
                12,
                RenownUpgradesGUI.class
        );
        RenownIconModel perks = RenownIconModel.create(
                Material.DIAMOND,
                Strings.Simple.RENOWN_PERKS_ICON.get(lang),
                getUpgradesLore(Arrays.asList(
                        Strings.Simple.RENOWN_PERKS_DESC_1.get(lang),
                        Strings.Simple.RENOWN_PERKS_DESC_2.get(lang)
                ), RenownCategory.PERKS),
                14,
                RenownPerksGUI.class
        );

        getInventory().setItem(upgrades.getSlot(), upgrades.getItemStack());
        getInventory().setItem(perks.getSlot(), perks.getItemStack());
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
    }

    private List<String> getUpgradesLore(List<String> extraInfo, RenownCategory category) {
        Strings.Language lang = playerModel.language;
        List<RenownShop> all = RenownService.fromCategory(category);
        int totalUnlockedTiers = 0;
        int totalTiers = 0;
        List<RenownShop> lockedPerks = new ArrayList<>();

        for (RenownShop perk : all) {
            List<RenownTierModel> tiers = perk.getTiers();
            totalTiers += tiers.size();

            int unlockedTier = playerModel.getRenownPerkTier(perk);
            if (unlockedTier > 0) {
                totalUnlockedTiers += unlockedTier;
            }

            if (tiers.stream().noneMatch(tier -> tier.getPrestige() <= playerModel.getPrestige())) {
                lockedPerks.add(perk);
            }
        }

        List<String> lore = new ArrayList<>(extraInfo);
        lore.add("");
        int unknowns = lockedPerks.size();
        String unlocked = Strings.Formatted.RENOWN_UNLOCKED_COUNT.format(lang, totalUnlockedTiers, totalTiers);
        if (totalTiers == totalUnlockedTiers && totalTiers > 0) {
            unlocked += " " + Strings.Simple.RENOWN_NICE.get(lang);
        }
        lore.add(unlocked);

        if (unknowns > 0) {
            lore.add(Strings.Formatted.RENOWN_UNKNOWNS.format(lang, unknowns));
        }

        lore.addAll(Arrays.asList(
                "",
                Strings.Formatted.RENOWN_DISPLAY.format(lang, playerModel.getRenown()),
                "",
                Strings.Simple.RENOWN_CLICK_TO_BROWSE.get(lang)
        ));
        return lore;
    }
}
