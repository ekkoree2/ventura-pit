package eu.ventura.menu.panel;

import dev.kyro.arcticapi.gui.AGUI;
import dev.kyro.arcticapi.gui.AGUIPanel;
import eu.ventura.constants.Strings;
import eu.ventura.constants.Sounds;
import eu.ventura.menu.ChoosePerkGUI;
import eu.ventura.menu.ConfirmGUI;
import eu.ventura.menu.PermanentGUI;
import eu.ventura.model.PerkModel;
import eu.ventura.model.PlayerModel;
import eu.ventura.model.TriggerModel;
import eu.ventura.perks.Perk;
import eu.ventura.service.PerkService;
import eu.ventura.util.ItemHelper;
import eu.ventura.util.NBTHelper;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChoosePerkPanel extends AGUIPanel {
    private final PlayerModel playerModel;
    private final int triggerSlot;

    public ChoosePerkPanel(AGUI gui, int triggerSlot) {
        super(gui);
        this.triggerSlot = triggerSlot;
        this.playerModel = PlayerModel.getInstance(player);
    }

    @Override
    public String getName() {
        return Strings.Simple.CHOOSE_A_PERK_TITLE.get(player);
    }

    @Override
    public int getRows() {
        int rows = 4;
        int total = PerkService.getPerks().size();
        if (total > 0) {
            rows += (int) Math.ceil(total / 7.0);
        }
        return rows;
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

        if (clicked.getType() == Material.ARROW) {
            new PermanentGUI(player).open();
            return;
        }

        if (clicked.getType() == Material.DIAMOND_BLOCK) {
            Perk current = PerkService.getPerk(playerModel.equippedPerks.get(triggerSlot - 12));
            if (current != null) {
                current.onUnequip(player);
                Sounds.SUCCESS.play(player);
                playerModel.equippedPerks.remove(triggerSlot - 12);
                new PermanentGUI(player).open();
            }
            return;
        }

        String id = NBTHelper.getString(clicked, "pit-perk-menu-item");
        Perk instance = PerkService.getPerk(id);
        if (instance == null) {
            return;
        }

        new ChoosePerkGUI(player, triggerSlot).open();

        PerkModel perkModel = new PerkModel(playerModel, instance);
        TriggerModel trigger = perkModel.getTrigger();

        if (trigger.mode == TriggerModel.Mode.CONFIRM_PANEL) {
            Runnable task = perkModel.getTask(triggerSlot);
            String cost = String.format("§6%sg", NumberFormat.getInstance().format(instance.getGold()));
            new ConfirmGUI(
                    player, "§6" + instance.getDisplayName(), cost, new ChoosePerkGUI(player, triggerSlot), task, null
            ).open();
            return;
        }
        if (trigger.sound == Sounds.SUCCESS) {
            Perk current = PerkService.getPerk(playerModel.equippedPerks.get(triggerSlot - 12));
            if (current != null && !current.getId().equals(instance.getId())) {
                current.onUnequip(player);
            }
            perkModel.addPerk(triggerSlot);
            new PermanentGUI(player).open();
        }
        String message = trigger.message;
        if (message != null) {
            player.sendMessage(message);
        }
        Sounds.SoundEffect sound = trigger.sound;
        if (sound != null) {
            sound.play(player);
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        getInventory().setItem((getRows() - 1) * 9 + 4, ItemHelper.createItem(
                Material.ARROW,
                Strings.Simple.PERK_BACK.get(player),
                List.of(Strings.Simple.PERM_UPGRADES_BACK.get(player)),
                true
        ));

        if (playerModel.equippedPerks.get(triggerSlot - 12) != null) {
            getInventory().setItem(getRows() * 9 - 4, ItemHelper.createItem(
                    Material.DIAMOND_BLOCK,
                    Strings.Simple.PERK_NO_PERK.get(player),
                    Arrays.asList(
                            Strings.Simple.PERK_HARDCORE.get(player),
                            Strings.Simple.PERK_DONT_NEED.get(player),
                            "",
                            Strings.Simple.PERK_CLICK_TO_REMOVE.get(player)
                    ),
                    true
            ));
        }
        List<Perk> perks = new ArrayList<>(PerkService.getSortedPerks());
        int slot = 10;

        for (Perk perk : perks) {
            if (slot >= (getRows() - 1) * 9) {
                break;
            }

            if (slot % 9 == 8 || slot % 9 == 0) {
                slot += 2;
            }

            PerkModel perkModel = new PerkModel(playerModel, perk);
            ItemStack common = perk.getBaseItem();
            ItemStack item = perkModel.getItemStack(common);
            if (item != null) {
                item = NBTHelper.setString(item, "pit-perk-menu-item", perk.getId());
                getInventory().setItem(slot, item);
            }
            slot++;
        }
    }

    @Override
    public void onClose(InventoryCloseEvent event) {

    }
}
