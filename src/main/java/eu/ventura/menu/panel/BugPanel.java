package eu.ventura.menu.panel;

import dev.kyro.arcticapi.gui.AGUI;
import dev.kyro.arcticapi.gui.AGUIPanel;
import eu.ventura.constants.NumberFormat;
import eu.ventura.menu.BugGUI;
import eu.ventura.model.BugModel;
import eu.ventura.service.BugService;
import eu.ventura.util.ItemHelper;
import eu.ventura.util.LoreBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * author: ekkoree
 * created at: 1/22/2026
 */
public class BugPanel extends AGUIPanel {
    private final int page;

    public BugPanel(AGUI gui, int page) {
        super(gui);
        this.page = page;
    }

    @Override
    public String getName() {
        int totalPages = getTotalPages();
        if (totalPages <= 1) {
            return "§8Bug Reports";
        }
        int currentPage = Math.max(1, page);
        return "§8(" + currentPage + "/" + totalPages + ") Bug Reports";
    }

    @Override
    public int getRows() {
        int count = BugService.getCount();
        if (count <= 7) return 3;
        return 4;
    }

    private int getTotalPages() {
        int count = BugService.getCount();
        if (count == 0) return 1;
        return (int) Math.ceil((double) count / 7);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        if (!getInventory().equals(event.getClickedInventory())) {
            return;
        }

        int slot = event.getSlot();
        int lastRow = getRows() - 1;
        int lastSlot = getRows() * 9 - 1;
        int firstSlotLastRow = lastRow * 9;

        if (slot == lastSlot && page < getTotalPages()) {
            new BugGUI(player, page + 1).open();
            return;
        }

        if (slot == firstSlotLastRow && page > 1) {
            new BugGUI(player, page - 1).open();
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        List<BugModel> bugs = BugService.getAllBugs();
        int startIndex = (page - 1) * 7;
        int endIndex = Math.min(startIndex + 7, bugs.size());

        int slot = 10;
        for (int i = startIndex; i < endIndex; i++) {
            BugModel bug = bugs.get(i);
            ItemStack head = ItemHelper.getPlayerHead(bug.getUuid());
            ItemStack item = ItemHelper.setItemMeta(
                    head,
                    "§eReport #" + bug.getId(),
                    new LoreBuilder()
                            .addNewline("§7Author: " + bug.getRankColor() + bug.getName())
                            .addNewline("§7Content: §f" + bug.getIssue())
                            .compile(),
                    false,
                    true
            );
            getInventory().setItem(slot, item);
            slot++;
        }

        int lastRow = getRows() - 1;
        int lastSlot = getRows() * 9 - 1;
        int firstSlotLastRow = lastRow * 9;
        int totalPages = getTotalPages();

        getInventory().setItem(lastSlot - 4, ItemHelper.createItem(
                Material.OAK_SIGN,
                "§aReport Statistics",
                new LoreBuilder()
                        .addNewline()
                        .addNewline("§eTotal reports: §a" + NumberFormat.DEF.of(BugService.getCount()))
                        .addNewline("§ePages: §a" + NumberFormat.DEF.of(totalPages))
                        .compile(),
                true
        ));

        if (page < totalPages) {
            getInventory().setItem(lastSlot, ItemHelper.createItem(
                    Material.ARROW,
                    "§aNext Page",
                    new LoreBuilder()
                            .addNewline("§ePage " + (page + 1))
                            .compile(),
                    true
            ));
        }

        if (page > 1) {
            getInventory().setItem(firstSlotLastRow, ItemHelper.createItem(
                    Material.ARROW,
                    "§aLast Page",
                    new LoreBuilder()
                            .addNewline("§ePage " + (page - 1))
                            .compile(),
                    true
            ));
        }
    }

    @Override
    public void onClose(InventoryCloseEvent event) {

    }
}
