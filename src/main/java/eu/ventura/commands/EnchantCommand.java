package eu.ventura.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import eu.ventura.enchantment.PitEnchant;
import eu.ventura.java.NewString;
import eu.ventura.service.EnchantmentService;
import eu.ventura.util.EnchantmentHelper;
import eu.ventura.util.NBTHelper;
import eu.ventura.util.NBTTag;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * author: ekkoree
 * created at: 1/15/2025
 */
@CommandPermission("rank.owner")
@CommandAlias("enchant|pitenchant")
public class EnchantCommand extends BaseCommand {
    @Default
    public void onEnchant(Player player, String enchantId, @Default("1") int level) {
        ItemStack item = player.getInventory().getItemInMainHand();
        PitEnchant enchant = EnchantmentService.getEnchantment(enchantId);

        if (enchant == null) {
            player.sendMessage(NewString.of("&c&lNOPE!&7 Enchantment not found!"));
            return;
        }
        item = EnchantmentHelper.addEnchant(item, enchantId, level, player);
        item = EnchantmentHelper.setTier(item, 1, player);

        item = NBTHelper.setString(item, NBTTag.MYSTIC_ITEM.getValue(), "true");
        item = EnchantmentHelper.refreshItem(item, player);
        item = EnchantmentHelper.setLives(item, 67, 69, player);

        player.getInventory().setItemInMainHand(item);

        player.sendMessage(NewString.of("&a&lADDED!&9 " + enchant.getDisplayName()));
    }
}
