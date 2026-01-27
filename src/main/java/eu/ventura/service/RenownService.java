package eu.ventura.service;

import eu.ventura.model.PlayerModel;
import eu.ventura.perks.permanent.Dirty;
import eu.ventura.perks.permanent.Rambo;
import eu.ventura.renown.RenownCategory;
import eu.ventura.renown.RenownShop;
import eu.ventura.renown.impl.perks.RenownPerk;
import eu.ventura.renown.impl.upgrades.Tenacity;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * author: ekkoree
 * created at: 1/24/2026
 */
public class RenownService {
    @Getter
    private static final Map<String, RenownShop> renownShop = new LinkedHashMap<>();

    static {
        addItem(new Tenacity());
        addItem(RenownPerk.of(Dirty::new));
        addItem(RenownPerk.of(Rambo::new));
    }

    public static <T extends RenownShop> T getInstance(Class<T> clazz) {
        for (RenownShop shop : renownShop.values()) {
            if (clazz.isInstance(shop)) {
                return clazz.cast(shop);
            }
        }
        return null;
    }

    public static void addItem(RenownShop item) {
        renownShop.put(item.getId(), item);
    }

    public static Map<RenownShop, Integer> getUpgrades(Inventory inventory, int rows, RenownCategory category) {
        Map<RenownShop, Integer> slots = new LinkedHashMap<>();
        Set<Integer> ignoredSlots = new HashSet<>();
        for (int row = 0; row < rows; row++) {
            int firstSlot = row * 9;
            int lastSlot = (row + 1) * 9 - 1;
            ignoredSlots.add(firstSlot);
            ignoredSlots.add(lastSlot);
        }
        List<RenownShop> sorted = new ArrayList<>();
        for (RenownShop shop : renownShop.values()) {
            if (shop.getCategory() == category) {
                sorted.add(shop);
            }
        }

        List<Integer> availableSlots = new ArrayList<>();
        int size = inventory.getSize();
        for (int i = 9; i < size; i++) {
            if (!ignoredSlots.contains(i) && inventory.getItem(i) == null) {
                availableSlots.add(i);
            }
        }
        for (int i = 0; i < sorted.size() && i < availableSlots.size(); i++) {
            slots.put(sorted.get(i), availableSlots.get(i));
        }
        return slots;
    }

    public static List<RenownShop> fromCategory(RenownCategory category) {
        List<RenownShop> sorted = new ArrayList<>();
        for (RenownShop item : renownShop.values()) {
            if (item.getCategory() == category) {
                sorted.add(item);
            }
        }
        return sorted;
    }

    public static RenownShop fromId(String id) {
        for (RenownShop item : renownShop.values()) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    public static RenownShop fromItem(Material icon, PlayerModel playerModel) {
        for (RenownShop item : renownShop.values()) {
            if (item.getIcon(playerModel).getType() == icon) {
                return item;
            }
        }
        return null;
    }
}
