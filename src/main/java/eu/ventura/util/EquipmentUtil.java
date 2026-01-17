package eu.ventura.util;

import eu.ventura.service.EquipmentService;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Arrays;

/**
 * author: ekkoree
 * created at: 01/17/2025
 */
public class EquipmentUtil {
    public static void giveDefaultGear(Player player) {
        PlayerInventory inventory = player.getInventory();

        ItemStack[] contents = inventory.getContents();
        ItemStack[] armor = inventory.getArmorContents();

        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (NBTHelper.getBoolean(item, "pit-default-item")) {
                contents[i] = new ItemStack(Material.AIR);
            }
        }
        for (int i = 0; i < armor.length; i++) {
            ItemStack item = armor[i];
            if (NBTHelper.getBoolean(item, "pit-default-item")) {
                armor[i] = new ItemStack(Material.AIR);
            }
        }

        player.getInventory().setContents(contents);
        player.getInventory().setArmorContents(armor);

        if (inventory.getBoots() == null || inventory.getBoots().getType() == Material.AIR) {
            inventory.setBoots(EquipmentService.BOOTS.create());
        }
        if (inventory.getLeggings() == null || inventory.getLeggings().getType() == Material.AIR) {
            inventory.setLeggings(EquipmentService.LEGGINGS.create());
        }
        if (inventory.getChestplate() == null || inventory.getChestplate().getType() == Material.AIR) {
            inventory.setChestplate(EquipmentService.CHESTPLATE.create());
        }

        if (Arrays.stream(inventory.getContents()).noneMatch(item -> item != null &&
                (item.getType().name().contains("SWORD") || item.getType().name().contains("AXE"))
        )) {
            ItemStack item = EquipmentService.SWORD.create();
            placeItem(inventory, item, 0);
        }

        if (Arrays.stream(inventory.getContents()).noneMatch(item -> item != null && item.getType() == Material.BOW)) {
            placeItem(inventory, EquipmentService.BOW.create(), 1);
        }

        if (Arrays.stream(inventory.getContents()).noneMatch(item -> item != null && item.getType() == Material.ARROW)) {
            placeItem(inventory, EquipmentService.ARROW.create(), 8);
        }
    }

    private static void placeItem(Inventory inventory, ItemStack item, int slot) {
        if (inventory.getItem(slot) != null) {
            int next = ItemHelper.getFreeSlot(inventory, false);
            if (next != -1) {
                inventory.setItem(next, inventory.getItem(slot));
            }
        }
        inventory.setItem(slot, item);
    }
}
