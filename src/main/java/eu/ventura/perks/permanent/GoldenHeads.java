package eu.ventura.perks.permanent;

import eu.ventura.Pit;
import eu.ventura.constants.Sounds;
import eu.ventura.constants.Strings;
import eu.ventura.enchantment.EnchantType;
import eu.ventura.event.PitKillEvent;
import eu.ventura.model.PlayerModel;
import eu.ventura.perks.Perk;
import eu.ventura.util.ItemHelper;
import eu.ventura.util.LoreBuilder;
import eu.ventura.util.NBTHelper;
import eu.ventura.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: ekkoree
 * created at: 1/18/2026
 */
public class GoldenHeads extends Perk {
    private static final String TEXTURE = "ewogICJ0ZXh0dXJlcyI6IHsKICAgICJTS0lOIjogewogICAgICAidXJsIjogImh0dHBzOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzRlNWIzMDhhMWViNWNhYTk3ZTVmYjI1N2IyZDllMTg2MWZkZWYxNTE2MWQ1MGExZjQ2ZjIyMzE1ZjQ5MjkiCiAgICB9CiAgfQp9";
    private final Map<Player, Long> cooldown = new HashMap<>();

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
        head = NBTHelper.setString(head, "pit-perk-item", getId());
        head = NBTHelper.setBoolean(head, "pit-restricted-item", true);
        return head;
    }

    private void applyEffects(Player player) {
        double abs = player.getAbsorptionAmount();
        double newAbs = Math.min(abs + 4, 6);

        if (newAbs > abs) {
            Bukkit.getScheduler().runTask(Pit.getInstance(), () -> {
                PlayerUtil.setAbs(player, newAbs);
            });
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 160, 0));
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        event.setCancelled(true);
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        long lastUse = cooldown.getOrDefault(player, 0L);
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastUse < 1000) {
            Sounds.GOLDEN_HEADS_COOLDOWN.play(player);
            return;
        }

        Sounds.GOLDEN_HEADS.play(player);
        item = ItemHelper.decreaseItemAmount(item);

        cooldown.put(player, currentTime);
        player.getInventory().setItemInMainHand(item);

        applyEffects(player);
    }

    @Override
    public void onKill(PitKillEvent event) {
        Player player = event.data.trueAttacker;
        PlayerModel model = PlayerModel.getInstance(player);
        Strings.Language language = model.getLanguage();

        int maxHeal = 64;

        for (ItemStack item : player.getInventory().getContents()) {
            if (getId().equals(NBTHelper.getString(item, "pit-perk-item"))) {
                if (item.getAmount() < maxHeal) {
                    item.setAmount(Math.min(item.getAmount() + 1, maxHeal));
                }
                return;
            }
        }

        ItemStack ghead = createItem(language);
        ghead.setAmount(1);
        player.getInventory().addItem(ghead);
    }
}
