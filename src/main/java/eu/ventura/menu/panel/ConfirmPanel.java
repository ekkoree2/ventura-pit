package eu.ventura.menu.panel;

import dev.kyro.arcticapi.gui.AGUI;
import dev.kyro.arcticapi.gui.AGUIPanel;
import eu.ventura.constants.Strings;
import eu.ventura.util.ItemHelper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class ConfirmPanel extends AGUIPanel {
    private final String title;
    private final String cost;
    private final AGUI returnGui;
    private final Runnable onConfirm;
    private final Runnable onCancel;

    public ConfirmPanel(AGUI gui, String title, String cost, AGUI returnGui, Runnable onConfirm, Runnable onCancel) {
        super(gui);
        this.title = title;
        this.cost = cost;
        this.returnGui = returnGui;
        this.onConfirm = onConfirm;
        this.onCancel = onCancel;
    }

    @Override
    public String getName() {
        return title != null ? title : "";
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

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null) {
            return;
        }

        if (clicked.getType() == Material.LIME_STAINED_GLASS_PANE) {
            if (onConfirm != null) {
                onConfirm.run();
            }
            return;
        }

        if (clicked.getType() == Material.RED_STAINED_GLASS_PANE) {
            if (onCancel != null) {
                onCancel.run();
            } else {
                returnGui.open();
            }
            return;
        }

        if (clicked.getType() == Material.ARROW) {
            returnGui.open();
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        String displayTitle = title != null ? title : "";
        String displayCost = cost != null ? cost : "";

        getInventory().setItem(11, ItemHelper.createItem(
                Material.LIME_STAINED_GLASS_PANE,
                Strings.Simple.CONFIRM_CONFIRM.get(player),
                Arrays.asList(
                        Strings.Simple.CONFIRM_CONFIRM_DESC.get(player),
                        "",
                        "§7" + displayTitle,
                        "§7" + displayCost
                ),
                true
        ));

        getInventory().setItem(13, ItemHelper.createItem(
                Material.ARROW,
                Strings.Simple.CONFIRM_CANCEL.get(player),
                Arrays.asList(Strings.Simple.CONFIRM_CANCEL_DESC.get(player)),
                true
        ));

        getInventory().setItem(15, ItemHelper.createItem(
                Material.RED_STAINED_GLASS_PANE,
                Strings.Simple.CONFIRM_CANCEL_TITLE.get(player),
                Arrays.asList(
                        Strings.Simple.CONFIRM_CANCEL_TITLE_DESC.get(player),
                        "",
                        "§7" + displayTitle,
                        "§7" + displayCost
                ),
                true
        ));
    }

    @Override
    public void onClose(InventoryCloseEvent event) {

    }
}
