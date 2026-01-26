package eu.ventura.menu.renown;

import dev.kyro.arcticapi.gui.AGUI;
import eu.ventura.menu.renown.panel.RenownPerksParent;
import org.bukkit.entity.Player;

/**
 * author: ekkoree
 * created at: 1/24/2026
 */
public class RenownPerksGUI extends AGUI {
    public RenownPerksGUI(Player player) {
        super(player);
        setHomePanel(new RenownPerksParent(this));
    }
}
