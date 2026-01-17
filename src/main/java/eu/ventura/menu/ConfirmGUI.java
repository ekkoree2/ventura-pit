package eu.ventura.menu;

import dev.kyro.arcticapi.gui.AGUI;
import eu.ventura.menu.panel.ConfirmPanel;
import org.bukkit.entity.Player;

public class ConfirmGUI extends AGUI {
    public ConfirmGUI(Player player, String title, String cost, AGUI returnGui, Runnable onConfirm, Runnable onCancel) {
        super(player);
        setHomePanel(new ConfirmPanel(this, title, cost, returnGui, onConfirm, onCancel));
    }
}
