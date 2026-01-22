package eu.ventura.model;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import eu.ventura.annotations.Save;
import eu.ventura.constants.Sounds;
import eu.ventura.constants.Status;
import eu.ventura.constants.Strings;
import eu.ventura.service.PerkService;
import eu.ventura.service.PlayerService;
import eu.ventura.util.LevelUtil;
import eu.ventura.util.MongoUtil;
import eu.ventura.util.PlayerUtil;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * author: ekkoree
 * created at: 12/26/2025
 */
public class PlayerModel {
    public final Player player;

    public Player lastAttacker;

    @Save public int requiredXP = 15;
    @Save private int level = 1;
    @Save private int prestige = 0;
    @Save public int xp = 0;
    public Status status = Status.IDLING;
    public int combatTime = 0;
    public int multiKills = 0;
    public long lastKillTime = 0;
    @Setter
    @Getter
    public int bounty = 0;
    public int streak = 0;

    public final PlayerEffectsModel effectsModel;

    @Save private double gold = 0;
    @Save public Strings.Language language = Strings.Language.POLISH;

    @Save public Set<String> purchasedPerks = new HashSet<>();
    @Save public Map<Integer, String> equippedPerks = new HashMap<>();

    @Save private ItemStack[] inventory = new ItemStack[36];
    @Save private ItemStack[] armor = new ItemStack[4];
    @Save private ItemStack[] enderChest;

    public PlayerModel(Player player) {
        this.player = player;
        this.effectsModel = new PlayerEffectsModel(this);
        this.enderChest = new ItemStack[getEnderChestRows() * 9];
    }

    public int getEnderChestRows() {
        return 3;
    }

    public ItemStack[] getEnderChest() {
        int rows = getEnderChestRows();
        int size = rows * 9;

        if (enderChest == null) {
            enderChest = new ItemStack[size];
        } else if (enderChest.length < size) {
            ItemStack[] newChest = new ItemStack[size];
            System.arraycopy(enderChest, 0, newChest, 0, enderChest.length);
            enderChest = newChest;
        }
        return enderChest;
    }

    public void setEnderChest(ItemStack[] items) {
        this.enderChest = items;
    }

    public static PlayerModel getInstance(Player player) {
        return PlayerService.getPlayer(player);
    }

    public void setLevel(int val) {
        this.level = Math.max(1, Math.min(120, val));
    }

    public int getLevel() {
        return Math.max(1, Math.min(120, level));
    }

    public int getPrestige() {
        return Math.max(0, Math.min(35, prestige));
    }

    public void setPrestige(int x) {
        this.prestige = Math.min(35, x);
    }

    public void setGold(double x) {
        this.gold = Math.max(0, Math.min(Integer.MAX_VALUE, x));
    }

    public double getGold() {
        if (Double.isNaN(gold)) {
            gold = 100;
        }
        return Math.min(Integer.MAX_VALUE, gold);
    }

    public void addXp(int xp) {
        if (level >= 120) {
            return;
        }

        this.xp += xp;
        requiredXP -= xp;
        int previousLevel = level;
        while (requiredXP <= 0 && level < 120) {
            requiredXP += LevelUtil.xpToNextLevel(prestige, level);
            level++;
        }
        if (previousLevel != level) {
            String levelProgression = String.format("%s §7➟ %s",
                    LevelUtil.getFormattedLevelFromValues(prestige, previousLevel),
                    LevelUtil.getFormattedLevelFromValues(prestige, level)
            );
            PlayerUtil.sendTitle(player, Strings.Formatted.LEVEL_UP_TITLE.format(language), levelProgression);
            Sounds.LEVEL_UP.play(player);
            player.sendMessage(Strings.Formatted.LEVEL_UP_MESSAGE.format(language, levelProgression));
        }
    }

    public void updateStatus() {
        if (combatTime > 0) {
            status = Status.FIGHTING;
        } else {
            status = bounty > 0 ? Status.BOUNTIED : Status.IDLING;
        }
    }

    public void runCombatTime() {
        int extraTime = (bounty / 1000) * 5;
        combatTime = 15 + extraTime;
    }

    public int getMaxBounty() {
        return 5000;
    }

    public void addGold(double gold) {
        this.gold += gold;
        if (Double.isNaN(this.gold) || Double.isInfinite(this.gold)) {
            this.gold = 100;
        }
    }

    public int getMultiKillsNumber() {
        return multiKills;
    }

    public void addKill() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastKillTime < 5000) {
            multiKills++;
        } else {
            multiKills = 1;
        }
        lastKillTime = currentTime;
    }

    public boolean hasPerk(String perkId) {
        return purchasedPerks.contains(perkId);
    }

    public void addPerk(String perkId) {
        purchasedPerks.add(perkId);
    }

    public void removePerk(String perkId) {
        purchasedPerks.remove(perkId);
        equippedPerks.values().remove(perkId);
    }

    public void equipPerk(int slot, String perkId) {
        if (!purchasedPerks.contains(perkId)) {
            return;
        }
        equippedPerks.put(slot, perkId);
        PerkService.getPerk(perkId).onEquip(player);
    }

    public void unequipPerk(int slot) {
        String perkId = equippedPerks.remove(slot);
        if (perkId != null) {
            PerkService.getPerk(perkId).onUnequip(player);
        }
    }

    public eu.ventura.perks.Perk getEquippedPerk(int slot) {
        String perkId = equippedPerks.get(slot);
        if (perkId == null) {
            return null;
        }
        return PerkService.getPerk(perkId);
    }

    public List<PerkSlotModel> getPerkSlots() {
        List<PerkSlotModel> slots = new ArrayList<>();
        slots.add(new PerkSlotModel(0, 10));
        slots.add(new PerkSlotModel(1, 35));
        slots.add(new PerkSlotModel(2, 70));
        return slots;
    }

    public boolean hasHealingPerk() {
        for (int slot : equippedPerks.keySet()) {
            eu.ventura.perks.Perk perk = getEquippedPerk(slot);
            if (perk != null && perk.isHealing()) {
                return true;
            }
        }
        return false;
    }

    public void setItems() {
        this.inventory = player.getInventory().getContents().clone();
        this.armor = player.getInventory().getArmorContents().clone();
    }

    public void getItems() {
        player.getInventory().clear();
        player.getInventory().setContents(inventory);
        player.getInventory().setArmorContents(new ItemStack[4]);
        player.getInventory().setArmorContents(armor);

        int rows = getEnderChestRows();
        int size = rows * 9;

        if (enderChest == null) {
            enderChest = new ItemStack[size];
        } else if (enderChest.length < size) {
            ItemStack[] newChest = new ItemStack[size];
            System.arraycopy(enderChest, 0, newChest, 0, enderChest.length);
            enderChest = newChest;
        }
    }

    private String serializeItemStackArray(ItemStack[] items) {
        if (items == null) return null;
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            BukkitObjectOutputStream out = new BukkitObjectOutputStream(stream);
            out.writeObject(items);
            out.close();
            return Base64.getEncoder().encodeToString(stream.toByteArray());
        } catch (Exception e) {
            return null;
        }
    }

    private ItemStack[] deserializeItemStackArray(String encoded) {
        if (encoded == null) return null;
        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(Base64.getDecoder().decode(encoded));
            BukkitObjectInputStream in = new BukkitObjectInputStream(stream);
            ItemStack[] items = (ItemStack[]) in.readObject();
            in.close();
            return items;
        } catch (Exception e) {
            return null;
        }
    }

    public void save() {
        setItems();
        MongoCollection<Document> collection = MongoUtil.getCollection("players");
        Document doc = new Document("_id", player.getUniqueId().toString());

        for (Field field : this.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Save.class)) {
                field.setAccessible(true);
                try {
                    Object value = field.get(this);
                    if (value == null) {
                        doc.append(field.getName(), null);
                    } else if (value instanceof ItemStack[] itemStacks) {
                        doc.append(field.getName(), serializeItemStackArray(itemStacks));
                    } else if (field.getType().isEnum()) {
                        doc.append(field.getName(), ((Enum<?>) value).name());
                    } else if (value instanceof Set<?> set) {
                        doc.append(field.getName(), new ArrayList<>(set));
                    } else if (value instanceof Map<?, ?> map) {
                        Document mapDoc = new Document();
                        map.forEach((k, v) -> mapDoc.append(String.valueOf(k), v));
                        doc.append(field.getName(), mapDoc);
                    } else {
                        doc.append(field.getName(), value);
                    }
                } catch (IllegalAccessException e) {
                    Bukkit.getLogger().warning("Failed to access field " + field.getName());
                }
            }
        }

        collection.replaceOne(new Document("_id", player.getUniqueId().toString()), doc,
                new ReplaceOptions().upsert(true));
    }

    @SuppressWarnings("unchecked")
    public void load() {
        CompletableFuture<Void> loadFuture = CompletableFuture.runAsync(() -> {
            MongoCollection<Document> collection = MongoUtil.getCollection("players");
            Document doc = collection.find(new Document("_id", player.getUniqueId().toString())).first();

            if (doc != null) {
                for (Field field : this.getClass().getDeclaredFields()) {
                    if (field.isAnnotationPresent(Save.class)) {
                        field.setAccessible(true);
                        try {
                            if (doc.containsKey(field.getName())) {
                                Object value = doc.get(field.getName());

                                if (value == null) {
                                    field.set(this, null);
                                } else if (field.getType() == ItemStack[].class && value instanceof String encoded) {
                                    field.set(this, deserializeItemStackArray(encoded));
                                } else if (field.getType().isEnum() && value instanceof String) {
                                    @SuppressWarnings("rawtypes")
                                    Class<? extends Enum> enumType = (Class<? extends Enum>) field.getType();
                                    Object enumValue = Enum.valueOf(enumType, (String) value);
                                    field.set(this, enumValue);
                                } else if (Set.class.isAssignableFrom(field.getType()) && value instanceof List<?> list) {
                                    Set<Object> set = new HashSet<>(list);
                                    field.set(this, set);
                                } else if (Map.class.isAssignableFrom(field.getType()) && value instanceof Document mapDoc) {
                                    Map<Integer, String> map = new HashMap<>();
                                    mapDoc.forEach((k, v) -> map.put(Integer.parseInt(k), (String) v));
                                    field.set(this, map);
                                } else if (field.getType() == int.class && value instanceof Number num) {
                                    field.setInt(this, num.intValue());
                                } else if (field.getType() == double.class && value instanceof Number num) {
                                    field.setDouble(this, num.doubleValue());
                                } else {
                                    field.set(this, value);
                                }
                            }
                        } catch (IllegalAccessException e) {
                            Bukkit.getLogger().warning("Failed to set field " + field.getName());
                        }
                    }
                }
            }
        });

        try {
            loadFuture.get(5, TimeUnit.SECONDS);
            getItems();
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Bukkit.getLogger().warning("Failed to load player data for " + player.getName() + ": " + e.getMessage());
        }
    }
}
