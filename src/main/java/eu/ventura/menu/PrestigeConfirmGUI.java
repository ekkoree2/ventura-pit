package eu.ventura.menu;

import dev.kyro.arcticapi.gui.AGUI;
import eu.ventura.menu.panel.PrestigeConfirmPanel;
import org.bukkit.entity.Player;

/**
 * author: ekkoree
 * created at: 1/22/2026
 */
public class PrestigeConfirmGUI extends AGUI {
    public PrestigeConfirmGUI(Player player, int timer) {
        super(player);
        setHomePanel(new PrestigeConfirmPanel(this, timer));
    }
}
