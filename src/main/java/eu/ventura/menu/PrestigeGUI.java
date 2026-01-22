package eu.ventura.menu;

import dev.kyro.arcticapi.gui.AGUI;
import eu.ventura.menu.panel.PrestigePanel;
import org.bukkit.entity.Player;

/**
 * author: ekkoree
 * created at: 1/22/2026
 */
public class PrestigeGUI extends AGUI {
    public PrestigeGUI(Player player) {
        super(player);
        setHomePanel(new PrestigePanel(this));
    }
}
