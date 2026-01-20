package eu.ventura.model;

import eu.ventura.constants.Sounds;
import eu.ventura.constants.Strings;
import eu.ventura.shop.Shop;
import eu.ventura.util.LoreBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.util.List;

/**
 * author: ekkoree
 * created at: 1/20/2026
 */
public class ShopModel {
    private final Player player;
    private final Shop shop;

    public ShopModel(Player player, Shop shop) {
        this.player = player;
        this.shop = shop;
    }

    private Strings.Language getLanguage() {
        return PlayerModel.getInstance(player).language;
    }

    private String getColor() {
        return PlayerModel.getInstance(player).getGold() >= shop.getPrice() ? "§e" : "§c";
    }

    public TriggerModel getTrigger() {
        if (PlayerModel.getInstance(player).getGold() >= shop.getPrice()) {
            return new TriggerModel(
                    null,
                    Strings.Formatted.SHOP_PURCHASE.format(player, shop.getDisplayName(player)),
                    TriggerModel.Mode.CONFIRM_PANEL
            );
        }
        return new TriggerModel(
                Sounds.NO,
                Strings.Simple.SHOP_NOT_ENOUGH_GOLD.get(player),
                TriggerModel.Mode.PASS
        );
    }

    public String getDisplayName() {
        return getColor() + shop.getDisplayName(player);
    }

    public List<String> getLore() {
        String message = PlayerModel.getInstance(player).getGold() >= shop.getPrice()
                ? Strings.Simple.SHOP_CLICK_TO_PURCHASE.get(getLanguage())
                : Strings.Simple.SHOP_NOT_ENOUGH_GOLD.get(getLanguage());

        LoreBuilder builder = new LoreBuilder();

        if (shop.getMessageBoost(player) != null) {
            builder.addAll(shop.getMessageBoost(player));
        }
        builder.addNewline();
        builder.addNewline(Strings.Simple.SHOP_LOST_ON_DEATH.get(getLanguage()));
        builder.addNewline(Strings.Formatted.SHOP_COST.format(getLanguage(), NumberFormat.getInstance().format(shop.getPrice())));
        builder.addNewline(getColor() + message);

        return builder.compile();
    }

    public ItemStack createDisplayItem() {
        ItemStack item = shop.getItem();
        return eu.ventura.util.ItemHelper.setItemMeta(
                item,
                getDisplayName(),
                getLore(),
                true,
                true
        );
    }
}
