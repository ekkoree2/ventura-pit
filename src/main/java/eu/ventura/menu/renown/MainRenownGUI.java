package eu.ventura.menu.renown;

import dev.kyro.arcticapi.gui.AGUI;
import eu.ventura.menu.renown.panel.MainRenownPanel;
import org.bukkit.entity.Player;

/**
 * author: ekkoree
 * created at: 1/24/2026
 */
public class MainRenownGUI extends AGUI {
    public MainRenownGUI(Player player) {
        super(player);
        setHomePanel(new MainRenownPanel(this));
    }
}
