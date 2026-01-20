package eu.ventura.shop.impl;

import eu.ventura.constants.Strings;
import eu.ventura.shop.Shop;
import eu.ventura.util.ItemHelper;
import eu.ventura.util.NBTHelper;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

/**
 * author: ekkoree
 * created at: 1/19/2026
 */
public class Obsidian extends Shop {
    @Override
    public double getPrice() {
        return 40;
    }

    @Override
    public ItemStack getItem() {
        ItemStack item = ItemHelper.createItem(
                Material.OBSIDIAN,
                8,
                null,
                null,
                true
        );
        item = NBTHelper.setString(item, "pit-block-item", item.getType().toString());
        return item;
    }

    @Override
    public boolean canAutobuy() {
        return false;
    }

    @Override
    public boolean isArmor() {
        return false;
    }

    @Override
    public int getPrestige() {
        return 0;
    }

    @Override
    public String getDisplayName(Strings.Language language) {
        return Strings.Simple.SHOP_ITEM_OBSIDIAN.get(language);
    }

    @Override
    public String getId() {
        return "obsidian";
    }

    @Override
    public List<String> getMessageBoost(Strings.Language language) {
        return Collections.singletonList(Strings.Simple.SHOP_MSG_OBSIDIAN_DURATION.get(language));
    }

    @Override
    public int getSlot() {
        return 12;
    }
}
