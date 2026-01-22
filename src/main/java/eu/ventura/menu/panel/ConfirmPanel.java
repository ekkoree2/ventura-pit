package eu.ventura.menu.panel;

import dev.kyro.arcticapi.gui.AGUI;
import dev.kyro.arcticapi.gui.AGUIPanel;
import eu.ventura.constants.Strings;
import eu.ventura.util.ItemHelper;
import eu.ventura.util.LoreBuilder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

/**
 * author: ekkoree
 * created at: 1/18/2026
 */
@Getter
public class ConfirmPanel extends AGUIPanel {
    private final String buyingItem;
    private final String cost;
    private final AGUI lastGui;
    private final Runnable task;
    private final ItemStack icon;

    public ConfirmPanel(AGUI gui, String buyingItem, String cost, AGUI lastGui, Runnable task, ItemStack icon) {
        super(gui);
        this.buyingItem = buyingItem;
        this.cost = cost;
        this.lastGui = lastGui;
        this.task = task;
        this.icon = icon;
    }

    @Override
    public String getName() {
        return Strings.Simple.CONFIRM_PANEL_TITLE.get(player);
    }

    @Override
    public int getRows() {
        return 3;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        if (!getInventory().equals(event.getClickedInventory())) {
            return;
        }

        if (event.getSlot() == 11) {
            task.run();
        } else if (event.getSlot() == 15) {
            if (lastGui != null) {
                lastGui.open();
            } else {
                player.closeInventory();
            }
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        getInventory().setItem(11, confirmButton());
        getInventory().setItem(15, returnButton());
    }

    @Override
    public void onClose(InventoryCloseEvent event) {

    }

    private ItemStack confirmButton() {
        if (icon == null) {
            return ItemHelper.createItem(
                    Material.GREEN_TERRACOTTA,
                    Strings.Simple.CONFIRM_PANEL_CONFIRM.get(player),
                    new LoreBuilder()
                            .add(Strings.Simple.CONFIRM_PANEL_PURCHASING.get(player) + getBuyingItem())
                            .addNewline(Strings.Simple.CONFIRM_PANEL_COST.get(player) + getCost())
                            .compile(),
                    true
            );
        }
        return ItemHelper.setItemMeta(
                icon,
                Strings.Simple.CONFIRM_PANEL_CONFIRM.get(player),
                new LoreBuilder()
                        .add(Strings.Simple.CONFIRM_PANEL_PURCHASING.get(player) + getBuyingItem())
                        .addNewline(Strings.Simple.CONFIRM_PANEL_COST.get(player) + getCost())
                        .compile(),
                true,
                true
        );
    }

    private ItemStack returnButton() {
        return ItemHelper.createItem(
                Material.RED_TERRACOTTA,
                Strings.Simple.CONFIRM_PANEL_CANCEL.get(player),
                new LoreBuilder()
                        .add(Strings.Simple.CONFIRM_PANEL_RETURN.get(player))
                        .compile(),
                true
        );
    }
}
