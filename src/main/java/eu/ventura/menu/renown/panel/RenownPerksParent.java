package eu.ventura.menu.renown.panel;

import dev.kyro.arcticapi.gui.AGUI;
import eu.ventura.constants.Strings;
import eu.ventura.menu.renown.RenownPerksGUI;
import eu.ventura.model.PlayerModel;
import eu.ventura.model.game.impl.renown.impl.RenownPerksModel;
import eu.ventura.renown.RenownCategory;
import eu.ventura.renown.RenownShop;
import org.bukkit.entity.Player;

/**
 * author: ekkoree
 * created at: 1/24/2026
 */
public class RenownPerksParent extends RenownParent<RenownPerksModel> {
    public RenownPerksParent(AGUI gui) {
        super(gui);
    }

    @Override
    protected RenownCategory getCategory() {
        return RenownCategory.PERKS;
    }

    @Override
    protected RenownPerksModel createModel(PlayerModel playerModel, RenownShop upgrade) {
        return new RenownPerksModel(playerModel, upgrade);
    }

    @Override
    protected String getPanelTitle() {
        return Strings.Simple.RENOWN_PERKS_TITLE.get(player);
    }

    @Override
    protected AGUI getHomePanel(Player player) {
        return new RenownPerksGUI(player);
    }
}
