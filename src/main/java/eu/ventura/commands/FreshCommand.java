package eu.ventura.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import eu.ventura.java.NewString;
import eu.ventura.util.EnchantmentHelper;
import eu.ventura.util.ItemHelper;
import eu.ventura.util.NBTHelper;
import eu.ventura.util.NBTTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;

/**
 * author: ekkoree
 * created at: 1/15/2025
 */
@CommandAlias("fresh")
public class FreshCommand extends BaseCommand {
    @Subcommand("sword")
    public void onFresh(Player player) {
        ItemStack sword = ItemHelper.createItem(Material.GOLDEN_SWORD, 1, null, null, true);

        sword = NBTHelper.setString(sword, NBTTag.MYSTIC_ITEM.getValue(), "true");

        var meta = sword.getItemMeta();
        meta.displayName(Component.text("Mystic Sword").color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
        AttributeModifier damageModifier = new AttributeModifier(
                NamespacedKey.minecraft("attack_damage"),
                6.5,
                AttributeModifier.Operation.ADD_NUMBER,
                EquipmentSlotGroup.MAINHAND
        );

        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, damageModifier);
        sword.setItemMeta(meta);

        sword = EnchantmentHelper.refreshItem(sword, player);

        player.getInventory().setItemInMainHand(sword);
    }
}
