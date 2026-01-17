package eu.ventura.menu;

import dev.kyro.arcticapi.gui.AGUI;
import eu.ventura.menu.panel.PermanentPanel;
import org.bukkit.entity.Player;

public class PermanentGUI extends AGUI {
    public PermanentGUI(Player player) {
        super(player);
        setHomePanel(new PermanentPanel(this));
    }
}
