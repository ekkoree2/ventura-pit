package eu.ventura.menu.renown;

import dev.kyro.arcticapi.gui.AGUI;
import eu.ventura.menu.renown.panel.RenownUpgradesParent;
import org.bukkit.entity.Player;

/**
 * author: ekkoree
 * created at: 1/24/2026
 */
public class RenownUpgradesGUI extends AGUI {
    public RenownUpgradesGUI(Player player) {
        super(player);
        setHomePanel(new RenownUpgradesParent(this));
    }
}
