package eu.ventura.menu;

import dev.kyro.arcticapi.gui.AGUI;
import eu.ventura.menu.panel.BugPanel;
import org.bukkit.entity.Player;

/**
 * author: ekkoree
 * created at: 1/22/2026
 */
public class BugGUI extends AGUI {
    public BugGUI(Player player) {
        this(player, 1);
    }

    public BugGUI(Player player, int page) {
        super(player);
        setHomePanel(new BugPanel(this, page));
    }
}
