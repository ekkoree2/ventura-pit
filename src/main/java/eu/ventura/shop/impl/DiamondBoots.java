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
public class DiamondBoots extends Shop {
    @Override
    public double getPrice() {
        return 300;
    }

    @Override
    public ItemStack getItem() {
        ItemStack item = ItemHelper.createItem(
                Material.DIAMOND_BOOTS,
                null,
                null,
                false
        );
        item = NBTHelper.setString(item, "pit-shop-item", getId());
        return item;
    }

    @Override
    public boolean isArmor() {
        return true;
    }

    @Override
    public int getPrestige() {
        return 0;
    }

    @Override
    public String getDisplayName(Strings.Language language) {
        return Strings.Simple.SHOP_ITEM_DIAMOND_BOOTS.get(language);
    }

    @Override
    public String getId() {
        return "diamond-boots";
    }

    @Override
    public List<String> getMessageBoost(Strings.Language language) {
        return Collections.singletonList(Strings.Simple.SHOP_MSG_AUTO_EQUIP.get(language));
    }

    @Override
    public int getSlot() {
        return 15;
    }
}
