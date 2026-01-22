package eu.ventura.menu.panel;

import dev.kyro.arcticapi.gui.AGUI;
import dev.kyro.arcticapi.gui.AGUIPanel;
import eu.ventura.Pit;
import eu.ventura.constants.Strings;
import eu.ventura.model.PlayerModel;
import eu.ventura.service.PlayerService;
import eu.ventura.util.LoreBuilder;
import eu.ventura.util.MathUtil;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

/**
 * author: ekkoree
 * created at: 1/22/2026
 */
public class PrestigeConfirmPanel extends AGUIPanel {
    private int countdown;
    private BukkitRunnable countdownTask;
    private final PlayerModel playerModel = PlayerService.getPlayer(player);
    private final Strings.Language lang;

    public PrestigeConfirmPanel(AGUI gui, int timer) {
        super(gui);
        this.countdown = timer;
        this.lang = playerModel.language;
    }

    @Override
    public String getName() {
        return Strings.Simple.PRESTIGE_CONFIRM_TITLE.get(lang) + " (5)";
    }

    @Override
    public int getRows() {
        return 3;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        if (countdown == 0 && event.getSlot() == 11) {
            player.closeInventory();
            playerModel.prestige();
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        getInventory().setItem(11, getPrestigeIcon());
        if (countdownTask != null) {
            countdownTask.cancel();
        }
        countdownTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (countdown <= 0) {
                    cancel();
                    return;
                }

                countdown--;
                getInventory().setItem(11, getPrestigeIcon());
                String title = countdown > 0 ? " (" + countdown + ")" : "";
                updateInventoryTitle(Strings.Simple.PRESTIGE_CONFIRM_TITLE.get(lang) + title);
            }
        };
        countdownTask.runTaskTimer(Pit.instance, 20, 20);
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        if (countdownTask != null) {
            countdownTask.cancel();
        }
    }

    private String getClickableTitle() {
        return countdown > 0 ? Strings.Simple.PRESTIGE_WAIT_AND_READ.get(lang) : Strings.Simple.PRESTIGE_CLICK_TO_PRESTIGE.get(lang);
    }

    private String getDisplayName() {
        return countdown > 0 ? Strings.Simple.PRESTIGE_ARE_YOU_SURE_YELLOW.get(lang) : Strings.Simple.PRESTIGE_ARE_YOU_SURE_GREEN.get(lang);
    }

    private List<String> getLore() {
        return new LoreBuilder()
                .add(Strings.Formatted.PRESTIGE_NEW_PRESTIGE.format(lang, MathUtil.roman(playerModel.getPrestige() + 1)))
                .addNewline()
                .addNewline(Strings.Simple.PRESTIGE_RESETTING_LEVEL.get(lang))
                .addNewline(Strings.Simple.PRESTIGE_RESETTING_PERKS.get(lang))
                .addNewline(Strings.Simple.PRESTIGE_RESETTING_UPGRADES.get(lang))
                .addNewline(Strings.Simple.PRESTIGE_RESETTING_INVENTORY.get(lang))
                .addNewline()
                .addNewline("§e" + getClickableTitle())
                .compile();
    }

    private ItemStack getPrestigeIcon() {
        Material material = countdown > 0 ? Material.YELLOW_TERRACOTTA : Material.LIME_TERRACOTTA;
        ItemStack stack = new ItemStack(material, 1);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(getDisplayName());
        meta.setLore(getLore());
        stack.setItemMeta(meta);
        return stack;
    }

    private void updateInventoryTitle(String newTitle) {
        player.getOpenInventory().setTitle(newTitle);
    }
}
