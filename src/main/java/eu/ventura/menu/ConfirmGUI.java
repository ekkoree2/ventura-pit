package eu.ventura.menu;

import dev.kyro.arcticapi.gui.AGUI;
import eu.ventura.menu.panel.ConfirmPanel;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * author: ekkoree
 * created at: 1/18/2026
 */
public class ConfirmGUI extends AGUI {
    public ConfirmGUI(Player player, String item, String cost, AGUI previous, Runnable task, ItemStack itemStack) {
        super(player);
        setHomePanel(new ConfirmPanel(this, item, cost, previous, task, itemStack));
    }
}
