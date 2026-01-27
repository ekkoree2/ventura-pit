package eu.ventura.menu.panel;


import dev.kyro.arcticapi.gui.AGUI;
import dev.kyro.arcticapi.gui.AGUIPanel;
import eu.ventura.model.PlayerModel;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * author: ekkoree
 * created at: 12/11/2025
 */
public class EnderChestMenu extends AGUIPanel {
    private final PlayerModel playerModel;
    private final Runnable onOpen;

    public EnderChestMenu(AGUI gui, Runnable onOpen) {
        super(gui);
        this.playerModel = PlayerModel.getInstance(player);
        this.onOpen = onOpen;
        this.cancelClicks = false;
    }

    @Override
    public String getName() {
        return "&8Ender Chest";
    }

    @Override
    public int getRows() {
        PlayerModel model = PlayerModel.getInstance(this.player);
        return model.getEnderChestRows();
    }

    @Override
    public void onClick(InventoryClickEvent event) {
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        if (onOpen != null) {
            onOpen.run();
        }

        Inventory inv = getInventory();
        inv.clear();

        ItemStack[] stored = playerModel.getEnderChest();
        int size = playerModel.getEnderChestRows() * 9;

        if (stored == null || stored.length != size) {
            playerModel.setEnderChest(new ItemStack[size]);
            return;
        }

        for (int i = 0; i < size; i++) {
            if (stored[i] != null) {
                inv.setItem(i, stored[i].clone());
            }
        }
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 0.75f, 1.0f);

        Inventory inv = getInventory();
        int size = playerModel.getEnderChestRows() * 9;
        ItemStack[] contents = new ItemStack[size];

        for (int i = 0; i < size; i++) {
            ItemStack item = inv.getItem(i);
            contents[i] = (item != null && item.getType() != Material.AIR) ? item.clone() : null;
        }

        playerModel.setEnderChest(contents);
    }
}