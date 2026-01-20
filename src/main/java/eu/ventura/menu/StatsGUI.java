package eu.ventura.menu;

import dev.kyro.arcticapi.gui.AGUI;
import eu.ventura.menu.panel.StatsPanel;
import org.bukkit.entity.Player;

/**
* author: ekkoree
* created at: 1/20/2026
 */
public class StatsGUI extends AGUI {
    public StatsGUI(Player player, Player target) {
        super(player);
        setHomePanel(new StatsPanel(this, target));
    }
}
