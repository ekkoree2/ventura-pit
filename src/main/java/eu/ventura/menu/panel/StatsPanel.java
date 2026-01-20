package eu.ventura.menu.panel;

import dev.kyro.arcticapi.gui.AGUI;
import dev.kyro.arcticapi.gui.AGUIPanel;
import eu.ventura.constants.NumberFormat;
import eu.ventura.constants.Strings;
import eu.ventura.model.PlayerModel;
import eu.ventura.util.ItemHelper;
import eu.ventura.util.LevelUtil;
import eu.ventura.util.LoreBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

/**
* author: ekkoree
* created at: 1/20/2026
 */
public class StatsPanel extends AGUIPanel {
    private final Player target;

    public StatsPanel(AGUI gui, Player target) {
        super(gui, true);
        this.target = target;
        buildInventory();
    }

    @Override
    public String getName() {
        return target.getName() + "'s stats";
    }

    @Override
    public int getRows() {
        return 3;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        if (!getInventory().equals(event.getClickedInventory())) {
            return;
        }

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null) {
            return;
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        PlayerModel targetModel = PlayerModel.getInstance(target);

        getInventory().setItem(13, getXpBottle(targetModel));
    }

    @Override
    public void onClose(InventoryCloseEvent event) {

    }

    private ItemStack getXpBottle(PlayerModel model) {
        return ItemHelper.createItem(
                Material.EXPERIENCE_BOTTLE,
                "§aEdit Level",
                new LoreBuilder()
                        .addNewline("§fLevel: §7" + LevelUtil.getFormattedLevelFromValues(model))
                        .addNewline(model.level == 120 ?
                                "&fXP: &bMAXED!" :
                                "&fRequired XP: &b" + NumberFormat.DEF.of(model.requiredXP)
                        )
                        .addNewline()
                        .addNewline("§eClick to change level!")
                        .compile(),
                true
        );
    }
}
