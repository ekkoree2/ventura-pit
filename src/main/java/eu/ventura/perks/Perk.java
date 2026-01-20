package eu.ventura.perks;

import eu.ventura.constants.Strings;
import eu.ventura.enchantment.EnchantType;
import eu.ventura.event.PitAssistEvent;
import eu.ventura.event.PitDeathEvent;
import eu.ventura.event.PitKillEvent;
import eu.ventura.model.AttackModel;
import eu.ventura.model.PlayerModel;
import eu.ventura.util.ItemHelper;
import eu.ventura.util.LoreBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

@Setter
@Getter
public abstract class Perk {

    protected boolean cancelled = false;

    @SuppressWarnings("all")
    public boolean isCancelled() {
        if (cancelled) {
            cancelled = false;
            return true;
        }
        return false;
    }

    public abstract String getId();

    public abstract String getDisplayName(Strings.Language language);

    public abstract int getLevel();

    public abstract int getGold();

    public abstract int getRenownCost();

    public abstract int getPrestigeRequirement();

    public abstract EnchantType getType();

    public abstract LoreBuilder getDescription(Strings.Language language);

    public abstract ItemStack getIcon();

    public List<String> getDescription(Player player) {
        Strings.Language language = PlayerModel.getInstance(player).language;
        return getDescription(language).compile();
    }

    public ItemStack getBaseItem() {
        return getIcon();
    }

    public int getFinalLevel(PlayerModel model) {
        return getLevel();
    }

    public void apply(AttackModel model) {}

    public void onEquip(Player player) {}

    public void onUnequip(Player player) {}

    public boolean isHealing() {
        return false;
    }

    public void onKill(PitKillEvent event) {}

    public void onDeath(PitDeathEvent event) {}

    public void onAssist(PitAssistEvent event) {}

    public void onInteract(PlayerInteractEvent event) {}

    public void onSwing(PlayerAnimationEvent event) {}

    public ItemStack createItem(Strings.Language language) {
        return null;
    }
}
