package eu.ventura.perks.permanent;

import eu.ventura.Pit;
import eu.ventura.constants.Strings;
import eu.ventura.enchantment.EnchantType;
import eu.ventura.event.PitKillEvent;
import eu.ventura.model.PlayerModel;
import eu.ventura.perks.Perk;
import eu.ventura.util.LoreBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * author: ekkoree
 * created at: 1/26/2026
 */
public class Ignition extends Perk {
    @Override
    public String getId() {
        return "ingition";
    }

    @Override
    public String getDisplayName(Strings.Language language) {
        return Strings.Simple.IGNITION.get(language);
    }

    @Override
    public int getLevel() {
        return 40;
    }

    @Override
    public int getGold() {
        return 5000;
    }

    @Override
    public int getRenownCost() {
        return 0;
    }

    @Override
    public int getPrestigeRequirement() {
        return 0;
    }

    @Override
    public EnchantType getType() {
        return EnchantType.OFFENSIVE;
    }

    @Override
    public LoreBuilder getDescription(Strings.Language language) {
        return Strings.Lore.IGNITION.get(language);
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.EGG);
    }

    @Override
    public void onKill(PitKillEvent event) {
        if (Pit.event != null) {
            return;
        }
        PlayerModel model = PlayerModel.getInstance(event.data.trueAttacker);
        if (model.streak <= 4) {
            event.data.xp += 12;
            event.data.gold += 9;
        }
    }
}
