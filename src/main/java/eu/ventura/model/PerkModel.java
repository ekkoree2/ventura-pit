package eu.ventura.model;

import eu.ventura.constants.Strings;
import eu.ventura.constants.Sounds;
import eu.ventura.java.NewString;
import eu.ventura.menu.PermanentGUI;
import eu.ventura.perks.Perk;
import eu.ventura.service.PerkService;
import eu.ventura.util.ItemHelper;
import eu.ventura.util.LevelUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PerkModel {
    protected final Perk perk;
    protected final PlayerModel playerModel;

    public PerkModel(PlayerModel playerModel, Perk perk) {
        this.playerModel = playerModel;
        this.perk = perk;
    }

    public void addPerk(Integer slot) {
        if (perk.isHealing()) {
            Integer healingSlot = null;
            for (Map.Entry<Integer, String> entry : playerModel.equippedPerks.entrySet()) {
                Perk equippedPerk = PerkService.getPerk(entry.getValue());
                if (equippedPerk != null && equippedPerk.isHealing()) {
                    healingSlot = entry.getKey();
                    break;
                }
            }
            if (healingSlot != null) {
                Perk current = PerkService.getPerk(playerModel.equippedPerks.get(healingSlot));
                if (current != null && !current.getId().equals(perk.getId())) {
                    current.onUnequip(playerModel.player);
                }
                playerModel.equippedPerks.put(healingSlot, perk.getId());
            } else {
                playerModel.equippedPerks.put(slot - 12, perk.getId());
            }
        } else {
            Perk current = PerkService.getPerk(playerModel.equippedPerks.get(slot - 12));
            if (current != null && !current.getId().equals(perk.getId())) {
                current.onUnequip(playerModel.player);
            }
            playerModel.equippedPerks.put(slot - 12, perk.getId());
        }

//        if (playerModel.str > 0) {
//            playerModel.player.sendMessage("§a§lGONE!§7 Your streak has been reset!");
//            playerModel.setStreak(0);
//        }
        perk.onEquip(playerModel.player);
    }

    public Runnable getTask(Integer slot) {
        return () -> {
            playerModel.purchasedPerks.add(perk.getId());
            playerModel.gold -= perk.getGold();
            addPerk(slot);

            Sounds.ITEM_PURCHASE.play(playerModel.player);
            playerModel.player.sendMessage(Strings.Formatted.PERK_PURCHASE_MSG.format(playerModel.player, perk.getDisplayName(playerModel.getLanguage())));
            new PermanentGUI(playerModel.player).open();
        };
    }

    public TriggerModel getTrigger() {
        if (playerModel.equippedPerks.containsValue(perk.getId())) {
            return new TriggerModel(Sounds.ENDERMAN_NO, Strings.Simple.PERK_ALREADY_SELECTED.get(playerModel.player), TriggerModel.Mode.PASS);
        }
        if (playerModel.purchasedPerks.contains(perk.getId())) {
            return new TriggerModel(Sounds.SUCCESS, null, TriggerModel.Mode.PASS);
        }
        if (playerModel.getLevel() < perk.getFinalLevel(playerModel)) {
            return new TriggerModel(
                    Sounds.NO,
                    Strings.Simple.PERK_TOO_LOW_LEVEL.get(playerModel.player),
                    TriggerModel.Mode.PASS
            );
        }
        if (playerModel.gold < perk.getGold()) {
            return new TriggerModel(
                    Sounds.NO,
                    Strings.Simple.PERK_NOT_ENOUGH_GOLD.get(playerModel.player),
                    TriggerModel.Mode.PASS
            );
        }
        return new TriggerModel(null, null, TriggerModel.Mode.CONFIRM_PANEL);
    }

    private String getColor() {
        if (playerModel.equippedPerks.containsValue(perk.getId())) {
            return "§a";
        }
        if (playerModel.getLevel() < perk.getFinalLevel(playerModel)) {
            return "§c";
        }
        if (playerModel.gold >= perk.getGold() || playerModel.purchasedPerks.contains(perk.getId())) {
            return "§e";
        }
        return "§c";
    }

    public String getDisplayName() {
        return getColor() + perk.getDisplayName(playerModel.getLanguage());
    }

    public ItemStack getItemStack(ItemStack defaultIcon) {
        List<String> lore = getLore();

        if (perk.getPrestigeRequirement() > playerModel.prestige) {
            return null;
        }

        String requiredLevelText = Strings.Formatted.PERK_SLOT_LEVEL.format(playerModel.player, "");
        if (lore.toString().contains(requiredLevelText) && playerModel.prestige == 0) {
            return createLockedIcon(Strings.Simple.PERK_SELECT_TITLE.get(playerModel.player), getLore());
        }
        String renownShopText = Strings.Simple.PERK_UNLOCKED_IN_RENOWN.get(playerModel.player);
        if (lore.toString().contains(renownShopText)) {
            return createLockedIcon(perk.getDisplayName(playerModel.getLanguage()), getLore());
        }
        return createItemStack(defaultIcon);
    }

    private ItemStack createItemStack(ItemStack defaultIcon) {
        return ItemHelper.createItem(defaultIcon.getType(), 1, getDisplayName(), getLore(), true);
    }

    private ItemStack createLockedIcon(String name, List<String> lore) {
        return ItemHelper.createItem(
                Material.BEDROCK,
                NewString.of("&c" + name),
                lore,
                true
        );
    }

    public List<String> getLore() {
        String color = getColor();

        if (playerModel.getLevel() < perk.getFinalLevel(playerModel)) {
            String formatted = LevelUtil.getFormattedLevelFromValues(playerModel.prestige, perk.getFinalLevel(playerModel));
            if (playerModel.prestige == 0) {
                return Collections.singletonList(Strings.Formatted.PERK_SLOT_LEVEL.format(playerModel.player, formatted));
            }
            return Collections.unmodifiableList(
                    new ArrayList<>() {{
                        addAll(perk.getDescription(playerModel.player));
                        add("");
                        add(Strings.Formatted.PERK_COST_FORMAT.format(playerModel.player, NumberFormat.getInstance().format(perk.getGold())));
                        add(Strings.Formatted.PERK_SLOT_LEVEL.format(playerModel.player, formatted));
                        add(Strings.Simple.PERK_TOO_LOW_LEVEL.get(playerModel.player));
                    }}
            );
        }

        List<String> lore = new ArrayList<>(perk.getDescription(playerModel.player));
        lore.add("");
        if (playerModel.equippedPerks.containsValue(perk.getId())) {
            lore.add(color + Strings.Simple.PERK_ALREADY_SELECTED.get(playerModel.player));
        } else {
            if (playerModel.purchasedPerks.contains(perk.getId())) {
                lore.add(color + Strings.Simple.PERK_CLICK_TO_SELECT.get(playerModel.player));
            } else {
                lore.add(Strings.Formatted.PERK_COST_FORMAT.format(playerModel.player, NumberFormat.getInstance().format(perk.getGold())));
                if (playerModel.gold >= perk.getGold()) {
                    lore.add(color + Strings.Simple.PERK_CLICK_TO_PURCHASE.get(playerModel.player));
                } else {
                    lore.add(color + Strings.Simple.PERK_NOT_ENOUGH_GOLD_DISPLAY.get(playerModel.player));
                }
            }
        }
        return lore;
    }
}
