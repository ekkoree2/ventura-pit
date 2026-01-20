package eu.ventura.service;

import eu.ventura.shop.Shop;
import eu.ventura.shop.impl.DiamondBoots;
import eu.ventura.shop.impl.DiamondChestplate;
import eu.ventura.shop.impl.DiamondSword;
import eu.ventura.shop.impl.Obsidian;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * author: ekkoree
 * created at: 1/19/2026
 */
public class ShopService {
    @Getter
    private static final Map<String, Shop> items = new HashMap<>();

    static {
        addItem(new DiamondSword());
        addItem(new Obsidian());
        addItem(new DiamondChestplate());
        addItem(new DiamondBoots());
    }

    @SuppressWarnings("unchecked")
    public static <T extends Shop> T getInstance(Class<T> clazz) {
        for (Shop shop : items.values()) {
            if (shop.getClass().equals(clazz)) {
                return (T) shop;
            }
        }
        return null;
    }

    public static Shop fromId(String id) {
        for (Shop item : items.values()) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    private static void addItem(Shop item) {
        items.put(item.getId(), item);
    }
}
