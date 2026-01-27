package eu.ventura.menu.panel;

import dev.kyro.arcticapi.gui.AGUIPanel;
import eu.ventura.menu.InvSeeGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * author: ekkoree
 * created at: 1/27/2026
 */
public class InvSeePanel extends AGUIPanel {
    public InvSeePanel(InvSeeGUI gui) {
        super(gui);
        this.cancelClicks = false;
    }

    private Player getTarget() {
        return ((InvSeeGUI) gui).getTarget();
    }

    @Override
    public String getName() {
        return getTarget().getName() + "'s inventory";
    }

    @Override
    public int getRows() {
        return 5;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if (!getInventory().equals(event.getClickedInventory())) {
            event.setCancelled(true);
            return;
        }
        int slot = event.getSlot();
        if (slot < 0 || slot >= 45) return;
        Player target = getTarget();
        ItemStack cursor = event.getCursor();
        if (slot < 36) {
            target.getInventory().setItem(slot, cursor != null && cursor.getType() != Material.AIR ? cursor.clone() : null);
        } else {
            int armorIndex = 3 - (slot - 36);
            ItemStack[] armor = target.getInventory().getArmorContents();
            armor[armorIndex] = cursor != null && cursor.getType() != Material.AIR ? cursor.clone() : null;
            target.getInventory().setArmorContents(armor);
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        Inventory inv = getInventory();
        inv.clear();
        Player target = getTarget();

        for (int i = 0; i < 36; i++) {
            ItemStack item = target.getInventory().getItem(i);
            if (item != null) {
                inv.setItem(i, item.clone());
            }
        }

        ItemStack[] armor = target.getInventory().getArmorContents();
        for (int i = 0; i < 4; i++) {
            if (armor[3 - i] != null) {
                inv.setItem(36 + i, armor[3 - i].clone());
            }
        }
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        Inventory inv = getInventory();
        Player target = getTarget();
        for (int i = 0; i < 36; i++) {
            ItemStack item = inv.getItem(i);
            if (item != null && item.getType() != Material.AIR) {
                target.getInventory().setItem(i, item.clone());
            } else {
                target.getInventory().setItem(i, null);
            }
        }
        target.getInventory().setArmorContents(getArmorContents(inv));
    }

    private ItemStack[] getArmorContents(Inventory inv) {
        ItemStack[] armor = new ItemStack[4];
        for (int i = 0; i < 4; i++) {
            ItemStack item = inv.getItem(36 + i);
            armor[3 - i] = (item != null && item.getType() != Material.AIR) ? item.clone() : null;
        }
        return armor;
    }
}
