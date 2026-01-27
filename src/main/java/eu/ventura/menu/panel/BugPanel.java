package eu.ventura.menu.panel;

import dev.kyro.arcticapi.gui.AGUI;
import dev.kyro.arcticapi.gui.AGUIPanel;
import eu.ventura.constants.NumberFormat;
import eu.ventura.constants.Sounds;
import eu.ventura.constants.Strings;
import eu.ventura.menu.BugGUI;
import eu.ventura.model.BugModel;
import eu.ventura.model.PlayerModel;
import eu.ventura.service.BugService;
import eu.ventura.util.ItemHelper;
import eu.ventura.util.LoreBuilder;
import eu.ventura.util.MongoUtil;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * author: ekkoree
 * created at: 1/22/2026
 */
public class BugPanel extends AGUIPanel {
    private final int page;
    private final Map<Integer, String> slotToBugId = new HashMap<>();

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
            return;
        }

        if (slotToBugId.containsKey(slot)) {
            String bugId = slotToBugId.get(slot);
            BugModel bug = BugService.getBug(bugId);
            if (bug == null) return;

            if (bug.isApproved()) {
                return;
            }

            if (event.getClick() == ClickType.RIGHT) {
                UUID reporterUuid = bug.getUuid();
                Player reporter = Bukkit.getPlayer(reporterUuid);

                if (reporter != null && reporter.isOnline()) {
                    PlayerModel playerModel = PlayerModel.getInstance(reporter);
                    playerModel.addXp(1000);
                    playerModel.addGold(3000);
                    reporter.sendMessage(Strings.Formatted.BUG_APPROVED.format(reporter, bug.getId()));
                } else {
                    Document doc = MongoUtil.getCollection("players").find(new Document("_id", reporterUuid.toString())).first();
                    if (doc != null) {
                        BugService.addPendingReward(reporterUuid.toString(), bug.getId(), 1000, 3000);
                    } else {
                        player.sendMessage(String.format(Strings.Simple.BUG_NOT_FOUND.get(player), bug.getName()));
                        return;
                    }
                }

                bug.setApproved(true);
                bug.save();
                new BugGUI(player, page).open();
                Sounds.SUCCESS.play(player);
            } else if (event.getClick() == ClickType.LEFT) {
                BugService.removeBug(bug.getId());
                new BugGUI(player, page).open();
                Sounds.BUG_REMOVE.play(player);
            }
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        slotToBugId.clear();
        List<BugModel> bugs = BugService.getAllBugs();
        int start = (page - 1) * 7;
        int end = Math.min(start + 7, bugs.size());

        int slot = 10;
        for (int i = start; i < end; i++) {
            BugModel bug = bugs.get(i);
            slotToBugId.put(slot, bug.getId());

            LoreBuilder loreBuilder = new LoreBuilder()
                    .addNewline("§7Author: " + bug.getRankColor() + bug.getName())
                    .addNewline("§7Content: §f" + bug.getIssue())
                    .addNewline();

            ItemStack icon;
            if (bug.isApproved()) {
                loreBuilder.addNewline("&aApproved!");
                icon = new ItemStack(Material.GREEN_TERRACOTTA);
            } else {
                loreBuilder.addNewline(Strings.Simple.BUG_RIGHT_CLICK.get(player)).addNewline(Strings.Simple.BUG_LEFT_CLICK.get(player));
                icon = ItemHelper.getPlayerHead(bug.getUuid());
            }

            ItemStack item = ItemHelper.setItemMeta(
                    icon,
                    "§eReport #" + bug.getId(),
                    loreBuilder.compile(),
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
