package eu.ventura.menu;

import dev.kyro.arcticapi.gui.AGUI;
import eu.ventura.menu.panel.EnderChestMenu;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

/**
 * author: ekkoree
 * created at: 1/26/2026
 */
public class EnderChestGUI extends AGUI {
    public EnderChestGUI(Player player, Runnable task) {
        super(player);
        setHomePanel(new EnderChestMenu(this, task));
    }
}
