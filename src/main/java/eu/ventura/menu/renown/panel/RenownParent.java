package eu.ventura.menu.renown.panel;

import dev.kyro.arcticapi.gui.AGUI;
import dev.kyro.arcticapi.gui.AGUIPanel;
import eu.ventura.constants.Strings;
import eu.ventura.menu.ConfirmGUI;
import eu.ventura.menu.renown.MainRenownGUI;
import eu.ventura.model.PlayerModel;
import eu.ventura.model.TriggerModel;
import eu.ventura.model.game.GameModel;
import eu.ventura.renown.RenownCategory;
import eu.ventura.renown.RenownShop;
import eu.ventura.service.PlayerService;
import eu.ventura.service.RenownService;
import eu.ventura.util.ItemHelper;
import eu.ventura.util.NBTHelper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * author: ekkoree
 * created at: 1/24/2026
 */
public abstract class RenownParent<T extends GameModel> extends AGUIPanel {
    protected PlayerModel playerModel;

    public RenownParent(AGUI gui) {
        super(gui);
        this.playerModel = PlayerService.getPlayer(player);
    }

    protected abstract RenownCategory getCategory();

    protected abstract T createModel(PlayerModel playerModel, RenownShop upgrade);

    protected abstract String getPanelTitle();

    protected abstract AGUI getHomePanel(Player player);

    private int getCurrentTier(RenownShop upgrade) {
        return playerModel.getRenownPerkTier(upgrade);
    }

    @Override
    public String getName() {
        return getPanelTitle();
    }

    @Override
    public int getRows() {
        return 5;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;

        if (clicked.getType() == Material.ARROW) {
            new MainRenownGUI(player).open();
            return;
        }

        String id = NBTHelper.getString(clicked, "pit-upgrade-item");
        if (id == null) {
            return;
        }

        RenownShop upgrade = RenownService.fromId(id);
        if (upgrade == null) {
            return;
        }

        T model = createModel(playerModel, upgrade);
        TriggerModel trigger = model.getTrigger();

        if (trigger.mode == TriggerModel.Mode.CONFIRM_PANEL) {
            Runnable task = model.getTask(null);
            int tier = getCurrentTier(upgrade);
            String cost = String.format("§e%d Renown", upgrade.getTier(tier + 1).getRenown());
            ConfirmGUI confirmGUI = new ConfirmGUI(
                    player, "§6" + upgrade.getDisplayName(), cost, getHomePanel(player), task, null
            );
            confirmGUI.open();
        } else {
            if (trigger.message != null) {
                player.sendMessage(trigger.message);
            }
            if (trigger.sound != null) {
                trigger.sound.play(player);
            }
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        getInventory().setItem((getRows() - 1) * 9 + 4, ItemHelper.getReturnMenu(player, Strings.Simple.RENOWN_SHOP_TITLE.get(playerModel.language)));

        Map<RenownShop, Integer> slots = RenownService.getUpgrades(getInventory(), getRows(), getCategory());
        for (Map.Entry<RenownShop, Integer> entry : slots.entrySet()) {
            T model = createModel(playerModel, entry.getKey());
            ItemStack icon = entry.getKey().getIcon(playerModel);
            if (entry.getKey().getTier(1).getPrestige() > playerModel.getPrestige()) {
                icon = new ItemStack(Material.BEDROCK);
            }
            ItemStack fixed = model.getItemStack(icon);
            fixed = NBTHelper.setString(fixed, "pit-upgrade-item", entry.getKey().getId());
            getInventory().setItem(entry.getValue(), fixed);
        }
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
    }
}
