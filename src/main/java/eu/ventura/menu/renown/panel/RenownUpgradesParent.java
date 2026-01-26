package eu.ventura.menu.renown.panel;

import dev.kyro.arcticapi.gui.AGUI;
import eu.ventura.menu.renown.RenownUpgradesGUI;
import eu.ventura.model.PlayerModel;
import eu.ventura.model.game.impl.renown.impl.RenownUpgradesModel;
import eu.ventura.renown.RenownCategory;
import eu.ventura.renown.RenownShop;
import org.bukkit.entity.Player;

/**
 * author: ekkoree
 * created at: 1/24/2026
 */
public class RenownUpgradesParent extends RenownParent<RenownUpgradesModel> {
    public RenownUpgradesParent(AGUI gui) {
        super(gui);
    }

    @Override
    protected RenownCategory getCategory() {
        return RenownCategory.UPGRADES;
    }

    @Override
    protected RenownUpgradesModel createModel(PlayerModel playerModel, RenownShop upgrade) {
        return new RenownUpgradesModel(playerModel, upgrade);
    }

    @Override
    protected String getPanelTitle() {
        return "§8Renown Upgrades";
    }

    @Override
    protected AGUI getHomePanel(Player player) {
        return new RenownUpgradesGUI(player);
    }
}
