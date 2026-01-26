package eu.ventura.model.game;

import eu.ventura.model.PlayerModel;
import eu.ventura.model.TriggerModel;
import eu.ventura.util.ItemHelper;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * author: ekkoree
 * created at: 1/24/2026
 */
@Getter
public abstract class GameModel {
    protected final PlayerModel playerModel;
    protected final Player player;

    protected GameModel(PlayerModel playerModel) {
        this.playerModel = playerModel;
        this.player = playerModel.player;
    }

    public Runnable getTask(Integer slot) {
        Runnable childTask = getChildTask(slot);

        return () -> {
            if (childTask != null) {
                childTask.run();
            }
        };
    }

    protected abstract Runnable getChildTask(Integer slot);

    public abstract TriggerModel getTrigger();

    public abstract String getDisplayName();

    public abstract List<String> getLore();

    public ItemStack getItemStack(ItemStack defaultIcon) {
        return ItemHelper.setItemMeta(
                defaultIcon,
                getDisplayName(),
                getLore(),
                false,
                true
        );
    }

    protected ItemStack createLockedIcon(String title, List<String> lore) {
        return ItemHelper.createItem(
                Material.BEDROCK,
                "§c" + title,
                lore,
                true
        );
    }
}
