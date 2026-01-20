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
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
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
    @Save public int level = 1;
    @Save public int prestige = 0;
    @Save public int xp = 0;
    public Status status = Status.IDLING;
    public int combatTime = 0;
    public int multiKills = 0;
    public long lastKillTime = 0;
    public int bounty = 0;
    public int streak = 0;

    public final PlayerEffectsModel effectsModel;

    @Save public double gold = 0;
    @Save public Strings.Language language = Strings.Language.POLISH;

    @Save public Set<String> purchasedPerks = new HashSet<>();
    @Save public Map<Integer, String> equippedPerks = new HashMap<>();

    public PlayerModel(Player player) {
        this.player = player;
        this.effectsModel = new PlayerEffectsModel(this);
    }

    public static PlayerModel getInstance(Player player) {
        return PlayerService.getPlayer(player);
    }

    public double getGold() {
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

    public int getBounty() {
        return bounty;
    }

    public void setBounty(int bounty) {
        this.bounty = bounty;
    }

    public int getMaxBounty() {
        return 5000;
    }

    public void addGold(double gold) {
        this.gold += gold;
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
        slots.add(new PerkSlotModel(0, 1));
        slots.add(new PerkSlotModel(1, 1));
        slots.add(new PerkSlotModel(2, 1));
        return slots;
    }

    public int getLevel() {
        return level;
    }

    @SuppressWarnings("unchecked")
    public void save() {
        MongoCollection<Document> collection = MongoUtil.getCollection("players");
        Document doc = new Document("_id", player.getUniqueId().toString());

        for (Field field : this.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Save.class)) {
                field.setAccessible(true);
                try {
                    Object value = field.get(this);
                    if (value != null && field.getType().isEnum()) {
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

                                if (value != null && field.getType().isEnum() && value instanceof String) {
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
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Bukkit.getLogger().warning("Failed to load player data for " + player.getName() + ": " + e.getMessage());
        }
    }
}
