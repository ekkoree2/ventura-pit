package eu.ventura.menu.renown.panel;

import dev.kyro.arcticapi.gui.AGUI;
import dev.kyro.arcticapi.gui.AGUIPanel;
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
    private final PlayerModel playerModel = PlayerService.getPlayer(player);

    public MainRenownPanel(AGUI gui) {
        super(gui);
    }

    @Override
    public String getName() {
        return "§8Renown Shop";
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
        RenownIconModel.clearIcons();
        getInventory().setItem((getRows() - 1) * 9 + 4, ItemHelper.getReturnMenu("Prestige & Renown"));

        RenownIconModel upgrades = RenownIconModel.create(
                Material.EMERALD,
                "§aUpgrades",
                getUpgradesLore(Arrays.asList(
                        "§7Variety of upgrades, buffs and",
                        "§7special unlocks."
                ), RenownCategory.UPGRADES),
                12,
                RenownUpgradesGUI.class
        );
        RenownIconModel perks = RenownIconModel.create(
                Material.DIAMOND,
                "§bPerks",
                getUpgradesLore(Arrays.asList(
                        "§7Unlock new perks available for",
                        "§7purchase at the upgrades npc."
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
        String unlocked = "§7Unlocked: §e" + totalUnlockedTiers + "/" + totalTiers;
        if (totalTiers == totalUnlockedTiers && totalTiers > 0) {
            unlocked += " §a§lNICE!!";
        }
        lore.add(unlocked);

        if (unknowns > 0) {
            lore.add("§8Unknowns: " + unknowns);
        }

        lore.addAll(Arrays.asList(
                "",
                "§7Renown: §e" + playerModel.getRenown() + " Renown",
                "",
                "§eClick to browse!"
        ));
        return lore;
    }
}
