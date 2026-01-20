package eu.ventura.menu;

import dev.kyro.arcticapi.gui.AGUI;
import eu.ventura.menu.panel.ShopPanel;
import org.bukkit.entity.Player;

/**
 * author: ekkoree
 * created at: 1/19/2026
 */
public class ShopGUI extends AGUI {
    public ShopGUI(Player player) {
        super(player);
        setHomePanel(new ShopPanel(this));
    }
}
