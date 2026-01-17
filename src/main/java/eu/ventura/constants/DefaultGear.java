package eu.ventura.constants;

import eu.ventura.util.ItemHelper;
import eu.ventura.util.NBTHelper;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

/**
 * author: ekkoree
 * created at: 01/17/2025
 */
public enum DefaultGear {
    BOOTS(new Material[]{Material.CHAINMAIL_BOOTS, Material.IRON_BOOTS}),
    LEGGINGS(new Material[]{Material.CHAINMAIL_LEGGINGS, Material.IRON_LEGGINGS}),
    CHESTPLATE(new Material[]{Material.CHAINMAIL_CHESTPLATE, Material.IRON_CHESTPLATE}),
    HELMET(new Material[]{Material.LEATHER_HELMET}),
    SWORD(new Material[]{Material.IRON_SWORD}),
    BOW(new Material[]{Material.BOW}),
    ARROW(new Material[]{Material.ARROW}, 32);

    private final Material[] materials;
    private final int amount;

    DefaultGear(Material[] materials) {
        this.materials = materials;
        this.amount = 1;
    }

    DefaultGear(Material[] materials, int amount) {
        this.materials = materials;
        this.amount = amount;
    }

    public ItemStack create() {
        Material selected = materials[ThreadLocalRandom.current().nextInt(materials.length)];
        ItemStack stack = ItemHelper.createItem(selected, null, null, false);
        stack.setAmount(amount);
        return NBTHelper.setBoolean(stack, "pit-default-item", true);
    }
}
