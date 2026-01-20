package eu.ventura.shop;

import eu.ventura.constants.Strings;
import eu.ventura.event.PitDamageEvent;
import eu.ventura.event.PitKillEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * author: ekkoree
 * created at: 1/19/2026
 */
public abstract class Shop {
    public abstract double getPrice();

    public abstract ItemStack getItem();

    public abstract int getPrestige();

    public abstract String getDisplayName(Strings.Language language);

    public abstract String getId();

    public abstract List<String> getMessageBoost(Strings.Language language);

    public abstract int getSlot();

    public abstract boolean isArmor();

    public String getDisplayName(Player player) {
        return getDisplayName(eu.ventura.model.PlayerModel.getInstance(player).language);
    }

    public List<String> getMessageBoost(Player player) {
        return getMessageBoost(eu.ventura.model.PlayerModel.getInstance(player).language);
    }

    public boolean canAutobuy() {
        return true;
    }

    public void onDamage(PitDamageEvent event) {

    }

    public void onDamaged(PitDamageEvent event) {

    }

    public void onKill(PitKillEvent event) {

    }

    public void onDrink(PlayerItemConsumeEvent event) {

    }
}
