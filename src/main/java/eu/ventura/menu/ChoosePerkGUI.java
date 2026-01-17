package eu.ventura.menu;

import dev.kyro.arcticapi.gui.AGUI;
import eu.ventura.menu.panel.ChoosePerkPanel;
import org.bukkit.entity.Player;

public class ChoosePerkGUI extends AGUI {
    public ChoosePerkGUI(Player player, Integer triggerSlot) {
        super(player);
        setHomePanel(new ChoosePerkPanel(this, triggerSlot));
    }
}
