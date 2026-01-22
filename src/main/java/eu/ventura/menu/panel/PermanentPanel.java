package eu.ventura.menu.panel;

import dev.kyro.arcticapi.gui.AGUI;
import dev.kyro.arcticapi.gui.AGUIPanel;
import eu.ventura.constants.Sounds;
import eu.ventura.constants.Strings;
import eu.ventura.java.NewString;
import eu.ventura.menu.ChoosePerkGUI;
import eu.ventura.model.PerkSlotModel;
import eu.ventura.model.PlayerModel;
import eu.ventura.perks.Perk;
import eu.ventura.util.ItemHelper;
import eu.ventura.util.LevelUtil;
import eu.ventura.util.LoreBuilder;
import io.papermc.paper.datacomponent.DataComponentTypes;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PermanentPanel extends AGUIPanel {
    private final PlayerModel playerModel = PlayerModel.getInstance(player);

    public PermanentPanel(AGUI gui) {
        super(gui);
    }

    @Override
    public String getName() {
        return Strings.Simple.PERM_UPGRADES.get(player);
    }

    @Override
    public int getRows() {
        return 3;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        if (!getInventory().equals(event.getClickedInventory())) {
            return;
        }

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null) {
            return;
        }

        int slot = event.getSlot();
        List<PerkSlotModel> slots = playerModel.getPerkSlots();
        for (PerkSlotModel perkSlot : slots) {
            if (perkSlot.getSlot() + 12 == slot) {
                if (clicked.getType() == Material.BEDROCK) {
                    player.sendMessage(Strings.Simple.PERK_SLOT_LOCKED_MSG.get(player));
                    Sounds.ENDERMAN_NO.play(player);
                } else {
                    new ChoosePerkGUI(player, slot).open();
                }
                return;
            }
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        if (playerModel.getLevel() < 10 && playerModel.getPrestige() == 0) {
            player.sendMessage(Strings.Simple.PERK_LEVEL_10_REQUIRED.get(player));
            Sounds.NO.play(player);
            event.setCancelled(true);
            return;
        }

        List<PerkSlotModel> slots = playerModel.getPerkSlots();
        for (PerkSlotModel perkSlot : slots) {
            int level = perkSlot.getRequiredLevel();
            int slot = perkSlot.getSlot() + 12;
            if (playerModel.getLevel() < level) {
                getInventory().setItem(slot, getUnknownSlot(perkSlot));
            } else {
                Perk equipped = playerModel.getEquippedPerk(perkSlot.getSlot());
                ItemStack block = equipped == null ? getDiamondBlock(perkSlot.getSlot()) : getPerkSlot(perkSlot.getSlot(), equipped);
                getInventory().setItem(slot, block);
            }
        }
    }

    @Override
    public void onClose(InventoryCloseEvent event) {

    }

    private ItemStack getUnknownSlot(PerkSlotModel model) {
        return ItemHelper.createItem(
                Material.BEDROCK,
                1,
                Strings.Formatted.PERK_SLOT_LOCKED_TITLE.format(playerModel.player, model.getSlot() + 1),
                new LoreBuilder()
                        .setMaxLength(35)
                        .addNewline(Strings.Formatted.PERK_SLOT_LEVEL.format(playerModel.player,
                                LevelUtil.getFormattedLevelFromValues(playerModel.getPrestige(), model.getRequiredLevel())
                        ))
                        .compile(),
                true
        );
    }

    private ItemStack getPerkSlot(int slotNum, Perk perk) {
        ItemStack base = ItemHelper.createItem(perk.getBaseItem(), 1);
        base.setData(DataComponentTypes.MAX_STACK_SIZE, 64);
        base.setAmount(slotNum + 1);
        return ItemHelper.setItemMeta(
                base,
                NewString.of("&ePerk Slot #" + (slotNum + 1)),
                new LoreBuilder()
                        .setMaxLength(35)
                        .addNewline(Strings.Formatted.PERK_SELECTED.format(playerModel.player, perk.getDisplayName(playerModel.language)))
                        .addNewline()
                        .addAll(perk.getDescription(player))
                        .addNewline()
                        .addNewline(Strings.Simple.PERK_CLICK_TO_CHOOSE.get(player))
                        .compile(),
                true,
                true
        );
    }

    private ItemStack getDiamondBlock(int slotNum) {
        return ItemHelper.createItem(
                Material.DIAMOND_BLOCK,
                Strings.Formatted.PERK_SLOT.format(playerModel.player, "&a", slotNum + 1),
                new LoreBuilder()
                        .addNewline(Strings.Simple.PERK_SELECT_A_PERK.get(player))
                        .addNewline()
                        .addNewline(Strings.Simple.PERK_CLICK_TO_CHOOSE.get(player))
                        .compile(),
                true
        );
    }
}
