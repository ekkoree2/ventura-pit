package eu.ventura.menu;

import dev.kyro.arcticapi.gui.AGUI;
import eu.ventura.menu.panel.InvSeePanel;
import lombok.Getter;
import org.bukkit.entity.Player;

/**
 * author: ekkoree
 * created at: 1/27/2026
 */
@Getter
public class InvSeeGUI extends AGUI {
    private final Player target;

    public InvSeeGUI(Player player, Player target) {
        super(player);
        this.target = target;
        setHomePanel(new InvSeePanel(this));
    }
}
