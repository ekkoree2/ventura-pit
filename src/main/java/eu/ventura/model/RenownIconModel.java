package eu.ventura.model;

import dev.kyro.arcticapi.gui.AGUI;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * author: ekkoree
 * created at: 1/24/2026
 */
@RequiredArgsConstructor
@Getter
public class RenownIconModel {
    private static final Set<RenownIconModel> icons = new HashSet<>();

    private final ItemStack stack;
    private final List<String> lore;
    private final String displayName;
    private final int slot;
    private final Class<? extends AGUI> guiClass;

    public ItemStack getItemStack() {
        ItemStack item = stack.clone();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public AGUI createGUI(Player player) {
        try {
            return guiClass.getConstructor(Player.class).newInstance(player);
        } catch (Exception ignore) {
            return null;
        }
    }

    public static RenownIconModel create(Material material, String displayName, List<String> lore, int slot, Class<? extends AGUI> guiClass) {
        RenownIconModel instance = new RenownIconModel(new ItemStack(material), lore, displayName, slot, guiClass);
        icons.add(instance);
        return instance;
    }

    public static Set<RenownIconModel> getIcons() {
        return icons;
    }

    public static void clearIcons() {
        icons.clear();
    }
}
