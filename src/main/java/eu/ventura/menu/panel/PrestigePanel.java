package eu.ventura.menu.panel;

import dev.kyro.arcticapi.gui.AGUI;
import dev.kyro.arcticapi.gui.AGUIPanel;
import eu.ventura.constants.Sounds;
import eu.ventura.constants.Strings;
import eu.ventura.menu.PrestigeConfirmGUI;
import eu.ventura.menu.renown.MainRenownGUI;
import eu.ventura.model.PlayerModel;
import eu.ventura.service.PlayerService;
import eu.ventura.util.ItemHelper;
import eu.ventura.util.LevelUtil;
import eu.ventura.util.LoreBuilder;
import eu.ventura.util.MathUtil;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * author: ekkoree
 * created at: 1/22/2026
 */
public class PrestigePanel extends AGUIPanel {
    private final PlayerModel playerModel = PlayerService.getPlayer(player);
    private final Strings.Language lang;

    public PrestigePanel(AGUI gui) {
        super(gui);
        this.lang = playerModel.language;
    }

    @Override
    public String getName() {
        return Strings.Simple.PRESTIGE_MENU_TITLE.get(lang);
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
        ItemStack stack = event.getCurrentItem();
        if (stack == null) {
            return;
        }

        if (stack.getType() == Material.DIAMOND) {
            if (playerModel.getPrestige() < 35 && playerModel.getLevel() == 120 && playerModel.getGoldReq() >= LevelUtil.getGoldReq(playerModel.getPrestige())) {
                new PrestigeConfirmGUI(player, 5).open();
            } else if (playerModel.getPrestige() >= 35) {
                player.closeInventory();
                player.sendMessage(Strings.Simple.PRESTIGE_MAX.get(lang));
            } else {
                player.closeInventory();
                Sounds.NO.play(player);
                if (playerModel.getLevel() != 120) {
                    player.sendMessage(Strings.Simple.PRESTIGE_TOO_LOW_LEVEL.get(lang));
                    player.sendMessage(Strings.Simple.PRESTIGE_TOO_LOW_LEVEL_DREAM.get(lang));
                } else if (playerModel.getGoldReq() < LevelUtil.getGoldReq(playerModel.getPrestige())) {
                    player.sendMessage(Strings.Formatted.PRESTIGE_NOT_ENOUGH_GOLD.format(
                            lang,
                            NumberFormat.getInstance().format((int) playerModel.getGoldReq()),
                            NumberFormat.getInstance().format((int) LevelUtil.getGoldReq(playerModel.getPrestige()))
                    ));
                    player.sendMessage(Strings.Simple.PRESTIGE_GRIND_MORE.get(lang));
                }
            }
        } else if (stack.getType() == Material.BEACON) {
            new MainRenownGUI(player).open();
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        if (playerModel.getPrestige() == 0 && playerModel.getLevel() < 120) {
            event.setCancelled(true);
            Sounds.NO.play(player);
            player.sendMessage(Strings.Simple.PRESTIGE_LEVEL_120_REQUIRED.get(lang));
            return;
        }
        getInventory().setItem(11, ItemHelper.createItem(
                Material.DIAMOND,
                Strings.Simple.PRESTIGE_MENU_BUTTON.get(lang),
                getLore(),
                true
        ));
//        getInventory().setItem(15, ItemHelper.createItem(
//                Material.BEACON,
//                Strings.Simple.PRESTIGE_RENOWN_SHOP.get(lang),
//                new LoreBuilder()
//                        .add(Strings.Formatted.PRESTIGE_RENOWN_SHOP_LORE_1.format(lang))
//                        .addNewline(Strings.Formatted.PRESTIGE_RENOWN_SHOP_LORE_2.format(lang))
//                        .addNewline(Strings.Formatted.PRESTIGE_RENOWN_SHOP_LORE_3.format(lang))
//                        .addNewline()
//                        .addNewline(Strings.Formatted.PRESTIGE_RENOWN_SHOP_LORE_4.format(lang))
//                        .addNewline(Strings.Formatted.PRESTIGE_RENOWN_SHOP_LORE_5.format(lang))
//                        .addNewline()
//                        .addNewline(Strings.Formatted.PRESTIGE_RENOWN_DISPLAY.format(lang, playerModel.getRenown()))
//                        .addNewline()
//                        .addNewline(Strings.Formatted.PRESTIGE_CLICK_TO_BROWSE.format(lang))
//                        .compile(),
//                true
//        ));
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
    }

    private List<String> getLore() {
        List<String> defaultLore = new ArrayList<>();
        if (playerModel.getPrestige() >= 35) {
            defaultLore.add(Strings.Simple.PRESTIGE_MAX_REACHED.get(lang));
            return defaultLore;
        }

        int prestige = playerModel.getPrestige();
        int level = playerModel.getLevel();
        long goldReq = (long) playerModel.getGoldReq();
        long requiredGold = (long) LevelUtil.getGoldReq(prestige);
        int renownReward = LevelUtil.getRenownFromPrestige(prestige + 1);

        String prestigeRoman = MathUtil.roman(prestige);
        String nextPrestigeRoman = MathUtil.roman(prestige + 1);
        String levelRequirement = Strings.Simple.PRESTIGE_LEVEL_REQ.get(lang) + LevelUtil.getBracketsColorChat(prestige) + "[" + LevelUtil.getLevelColorChat(120) + "120" + LevelUtil.getBracketsColorChat(prestige) + "]";

        if (playerModel.getPrestige() <= 0) {
            defaultLore.add(levelRequirement);
            defaultLore.add("");
            defaultLore.add(Strings.Simple.PRESTIGE_COST.get(lang));
        } else {
            defaultLore.add(Strings.Formatted.PRESTIGE_CURRENT.format(lang, prestigeRoman));
            if (level != 120) {
                defaultLore.add(Strings.Formatted.PRESTIGE_XP_PENALTY.format(lang, (int) LevelUtil.getPrestigeXpAmount(prestige) * 10));
                defaultLore.add("");
                defaultLore.add(levelRequirement);
                defaultLore.add(Strings.Simple.PRESTIGE_LEVEL_UP_TO_PRESTIGE.get(lang));
                return defaultLore;
            }
            defaultLore.add(levelRequirement);
            defaultLore.add("");
            defaultLore.add(Strings.Simple.PRESTIGE_COST.get(lang));
        }

        defaultLore.add(Strings.Simple.PRESTIGE_RESET_LEVEL.get(lang));
        defaultLore.add(Strings.Simple.PRESTIGE_RESET_GOLD.get(lang));
        defaultLore.add(Strings.Simple.PRESTIGE_RESET_PERKS.get(lang));
        defaultLore.add(Strings.Simple.PRESTIGE_RESET_INVENTORY.get(lang));
        defaultLore.add(Strings.Formatted.PRESTIGE_GOLD_GRINDED.format(lang, NumberFormat.getInstance().format(goldReq), NumberFormat.getInstance().format(requiredGold)));
        defaultLore.add(Strings.Simple.PRESTIGE_RENOWN_KEPT.get(lang));
        defaultLore.add(Strings.Simple.PRESTIGE_ENDERCHEST_KEPT.get(lang));
        defaultLore.add("");

        defaultLore.add(Strings.Formatted.PRESTIGE_RENOWN_REWARD.format(lang, renownReward));
        defaultLore.add("");
        defaultLore.add(Strings.Formatted.PRESTIGE_NEW_PRESTIGE.format(lang, nextPrestigeRoman));
        defaultLore.add(Strings.Formatted.PRESTIGE_XP_PENALTY.format(lang, (int) LevelUtil.getPrestigeXpAmount(prestige + 1) * 10));
        defaultLore.add("");
        defaultLore.add(Strings.Simple.PRESTIGE_CLICK_TO_PURCHASE.get(lang));
        return defaultLore;
    }
}
