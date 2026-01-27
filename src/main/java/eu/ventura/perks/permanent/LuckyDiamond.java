package eu.ventura.perks.permanent;

import eu.ventura.constants.Strings;
import eu.ventura.enchantment.EnchantType;
import eu.ventura.event.PitKillEvent;
import eu.ventura.model.PlayerModel;
import eu.ventura.perks.Perk;
import eu.ventura.util.ItemHelper;
import eu.ventura.util.LoreBuilder;
import eu.ventura.util.NBTHelper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;

/**
 * author: ekkoree
 * created at: 1/26/2026
 */
public class LuckyDiamond extends Perk {
    @Override
    public String getId() {
        return "lucky-diamond";
    }

    @Override
    public String getDisplayName(Strings.Language language) {
        return Strings.Simple.PERK_LUCKY_DIAMOND.get(language);
    }

    @Override
    public int getLevel() {
        return 40;
    }

    @Override
    public int getGold() {
        return 4000;
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
        return EnchantType.DEFENSIVE;
    }

    @Override
    public LoreBuilder getDescription(Strings.Language language) {
        return Strings.Lore.LUCKY_DIAMOND.get(language);
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.DIAMOND);
    }

    @Override
    public void onKill(PitKillEvent event) {
        if (Math.random() >= 0.09) {
            return;
        }

        Player player = event.data.trueAttacker;
        PlayerInventory inventory = player.getInventory();
        PlayerModel model = PlayerModel.getInstance(player);

        List<Integer> ironSlots = new ArrayList<>();
        ItemStack[] armor = inventory.getArmorContents();

        for (int i = 0; i < armor.length; i++) {
            if (armor[i] != null && canUpgrade(armor[i].getType())) {
                ironSlots.add(i);
            }
        }

        if (ironSlots.isEmpty()) {
            return;
        }

        int slot = ironSlots.get((int) (Math.random() * ironSlots.size()));
        ItemStack oldItem = armor[slot];
        Material diamondType = toDiamond(oldItem.getType());

        if (diamondType == null) {
            return;
        }

        ItemStack newItem = ItemHelper.createItem(
                diamondType,
                null,
                new LoreBuilder().add("&7Perk item").compile(),
                true
        );
        newItem = NBTHelper.setString(newItem, "pit-perk-item", getId());
        armor[slot] = newItem;
        inventory.setArmorContents(armor);

        String pieceName = toString(diamondType, model.language);
        player.sendMessage(Strings.Formatted.LUCKY_DIAMOND_MSG.format(player, pieceName));
    }

    private boolean canUpgrade(Material material) {
        return material == Material.IRON_CHESTPLATE ||
               material == Material.IRON_LEGGINGS ||
               material == Material.IRON_BOOTS ||
               material == Material.CHAINMAIL_CHESTPLATE ||
               material == Material.CHAINMAIL_LEGGINGS ||
               material == Material.CHAINMAIL_BOOTS;
    }

    private Material toDiamond(Material material) {
        return switch (material) {
            case IRON_CHESTPLATE, CHAINMAIL_CHESTPLATE -> Material.DIAMOND_CHESTPLATE;
            case IRON_LEGGINGS, CHAINMAIL_LEGGINGS -> Material.DIAMOND_LEGGINGS;
            case IRON_BOOTS, CHAINMAIL_BOOTS -> Material.DIAMOND_BOOTS;
            default -> null;
        };
    }

    private String toString(Material diamond, Strings.Language language) {
        boolean polish = language == Strings.Language.POLISH;
        return switch (diamond) {
            case DIAMOND_CHESTPLATE -> polish ? "Diamentowy Napierśnik" : "Diamond Chestplate";
            case DIAMOND_LEGGINGS -> polish ? "Diamentowe Spodnie" : "Diamond Leggings";
            case DIAMOND_BOOTS -> polish ? "Diamentowe Buty" : "Diamond Boots";
            default -> "";
        };
    }
}
