package eu.ventura.perks.permanent;

import eu.ventura.Pit;
import eu.ventura.constants.Strings;
import eu.ventura.enchantment.EnchantType;
import eu.ventura.event.PitKillEvent;
import eu.ventura.events.major.impl.RagePit;
import eu.ventura.model.PlayerModel;
import eu.ventura.perks.Perk;
import eu.ventura.util.ConsumableUtil;
import eu.ventura.util.ItemHelper;
import eu.ventura.util.LoreBuilder;
import eu.ventura.util.NBTHelper;
import eu.ventura.util.NBTTag;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * author: ekkoree
 * created at: 1/18/2026
 */
public class GoldenHeads extends Perk {
    private static final String TEXTURE = "ewogICJ0ZXh0dXJlcyI6IHsKICAgICJTS0lOIjogewogICAgICAidXJsIjogImh0dHBzOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzRlNWIzMDhhMWViNWNhYTk3ZTVmYjI1N2IyZDllMTg2MWZkZWYxNTE2MWQ1MGExZjQ2ZjIyMzE1ZjQ5MjkiCiAgICB9CiAgfQp9";

    @Override
    public String getId() {
        return "golden-heads";
    }

    @Override
    public String getDisplayName(Strings.Language language) {
        return Strings.Simple.PERK_GOLDEN_HEADS.get(language);
    }

    @Override
    public int getLevel() {
        return 10;
    }

    @Override
    public int getGold() {
        return 500;
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
        return Strings.Lore.GOLDEN_HEADS_DESC.get(language);
    }

    @Override
    public ItemStack getIcon() {
        return ItemHelper.createSkull(TEXTURE);
    }

    @Override
    public boolean isHealing() {
        return true;
    }

    @Override
    public ItemStack createItem(Strings.Language language) {
        ItemStack head = ItemHelper.createSkull(TEXTURE);
        head = ItemHelper.setItemMeta(head, Strings.Simple.GOLDEN_HEADS_NAME.get(language),
                Strings.Lore.GOLDEN_HEADS_ITEM.compile(language), true, true);
        head = NBTHelper.setString(head, NBTTag.PERK_ITEM.getValue(), getId());
        head = NBTHelper.setBoolean(head, NBTTag.RESTRICTED_ITEM.getValue(), true);
        return head;
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        event.setCancelled(true);
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        ConsumableUtil.tryConsume(player, item, getId(), 1000, ConsumableUtil::applyGoldenHeadsEffects);
    }

    @Override
    public void onKill(PitKillEvent event) {
        if (Pit.event instanceof RagePit) {
            return;
        }

        Player player = event.data.trueAttacker;
        PlayerModel model = PlayerModel.getInstance(player);
        Strings.Language language = model.language;

        ConsumableUtil.giveConsumableOnKill(player, NBTTag.PERK_ITEM.getValue(), getId(), createItem(language), 2);
    }
}
