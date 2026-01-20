package eu.ventura.shop.impl;

import eu.ventura.constants.Strings;
import eu.ventura.event.PitDamageEvent;
import eu.ventura.model.PlayerModel;
import eu.ventura.service.PlayerService;
import eu.ventura.shop.Shop;
import eu.ventura.util.ItemHelper;
import eu.ventura.util.NBTHelper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

/**
 * author: ekkoree
 * created at: 1/19/2026
 */
public class DiamondSword extends Shop {
    @Override
    public double getPrice() {
        return 150;
    }

    @Override
    public ItemStack getItem() {
        ItemStack item = ItemHelper.createItem(
                Material.DIAMOND_SWORD,
                null,
                null,
                false
        );
        return NBTHelper.setString(item, "pit-shop-item", getId());
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
        return Strings.Simple.SHOP_ITEM_DIAMOND_SWORD.get(language);
    }

    @Override
    public String getId() {
        return "diamond-sword";
    }

    @Override
    public List<String> getMessageBoost(Strings.Language language) {
        return Collections.singletonList(Strings.Simple.SHOP_MSG_DIAMOND_SWORD_BOOST.get(language));
    }

    @Override
    public int getSlot() {
        return 11;
    }

    @Override
    public void onDamage(PitDamageEvent event) {
        Player victim = event.getVictim();
        PlayerModel playerModel = PlayerService.getPlayer(victim);
        if (playerModel.getBounty() > 0) {
            event.attackModel.bountyHunterMultiplier += 0.2;
        }
    }
}
