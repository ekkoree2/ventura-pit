package eu.ventura.menu.panel;

import dev.kyro.arcticapi.gui.AGUI;
import dev.kyro.arcticapi.gui.AGUIPanel;
import eu.ventura.constants.Strings;
import eu.ventura.constants.Sounds;
import eu.ventura.menu.ShopGUI;
import eu.ventura.model.PlayerModel;
import eu.ventura.model.ShopModel;
import eu.ventura.service.ShopService;
import eu.ventura.shop.Shop;
import eu.ventura.util.ItemHelper;
import eu.ventura.util.NBTHelper;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 * author: ekkoree
 * created at: 1/19/2026
 */
public class ShopPanel extends AGUIPanel {
    private final PlayerModel playerModel = PlayerModel.getInstance(player);

    public ShopPanel(AGUI gui) {
        super(gui);
    }

    @Override
    public String getName() {
        return Strings.Simple.SHOP_TITLE.get(player);
    }

    @Override
    public int getRows() {
        return 3;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null) {
            return;
        }
        if (!getInventory().equals(event.getClickedInventory())) {
            return;
        }

        Material type = clicked.getType();
        Shop shop = ShopService.getItems().values().stream()
                .filter(s -> s.getItem().getType() == type)
                .findFirst()
                .orElse(null);
        if (shop == null) {
            return;
        }

        ShopModel shopModel = new ShopModel(player, shop);
        if (playerModel.getGold() < shop.getPrice()) {
            player.sendMessage(Strings.Simple.SHOP_NOT_ENOUGH_GOLD.get(player));
            Sounds.NO.play(player);
            return;
        }

        ItemStack stack = shop.getItem();
        int freeSlots = 0;
        for (ItemStack invItem : player.getInventory().getStorageContents()) {
            if (invItem == null || invItem.getType() == Material.AIR) {
                freeSlots++;
            }
        }

        int needed = 1;
        if (shop.isArmor()) {
            EquipmentSlot slot = ItemHelper.getArmorSlot(stack.getType());
            if (slot != null) {
                ItemStack existing = player.getInventory().getItem(slot);
                if (existing.getType() != Material.AIR) {
                    needed = 2;
                }
            }
        }

        if (freeSlots < needed) {
            Sounds.NO.play(player);
            player.closeInventory();
            player.sendMessage(Strings.Simple.SHOP_INVENTORY_FULL.get(player));
            return;
        }

        playerModel.gold -= shop.getPrice();

        if (shop.isArmor()) {
            EquipmentSlot slot = ItemHelper.getArmorSlot(stack.getType());
            if (slot != null) {
                ItemStack old = player.getInventory().getItem(slot);
                if (old.getType() != Material.AIR) {
                    player.getInventory().addItem(old);
                }
                player.getInventory().setItem(slot, stack);
            } else {
                player.getInventory().addItem(stack);
            }
        } else {
            int defaultSlot = findDefaultWeaponSlot();
            if (defaultSlot != -1) {
                player.getInventory().setItem(defaultSlot, stack);
            } else {
                player.getInventory().addItem(stack);
            }
        }

        Sounds.ITEM_PURCHASE.play(player);
        player.sendMessage(Strings.Formatted.SHOP_PURCHASE.format(player, shop.getDisplayName(playerModel.language)));
        new ShopGUI(player).open();
    }

    private int findDefaultWeaponSlot() {
        for (int i = 0; i < 9; i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item == null || item.getType() == Material.AIR) {
                continue;
            }
            if (!isWeapon(item.getType())) {
                continue;
            }
            if (NBTHelper.getBoolean(item, "pit-default-item")) {
                return i;
            }
        }
        return -1;
    }

    private boolean isWeapon(Material material) {
        String name = material.name();
        return name.endsWith("_SWORD") || name.endsWith("_AXE");
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        for (Shop shop : ShopService.getItems().values()) {
            int slot = shop.getSlot();
            ShopModel shopModel = new ShopModel(player, shop);
            getInventory().setItem(slot, shopModel.createDisplayItem());
        }
    }

    @Override
    public void onClose(InventoryCloseEvent event) {

    }
}
