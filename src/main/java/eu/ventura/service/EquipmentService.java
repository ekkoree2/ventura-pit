package eu.ventura.service;

import eu.ventura.constants.DefaultGear;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;

/**
 * author: ekkoree
 * created at: 01/17/2025
 */
@Getter
@RequiredArgsConstructor
public enum EquipmentService {
    BOOTS(DefaultGear.BOOTS),
    LEGGINGS(DefaultGear.LEGGINGS),
    CHESTPLATE(DefaultGear.CHESTPLATE),
    HELMET(DefaultGear.HELMET),
    SWORD(DefaultGear.SWORD),
    BOW(DefaultGear.BOW),
    ARROW(DefaultGear.ARROW);

    private final DefaultGear gear;

    public ItemStack create() {
        return gear.create();
    }
}
