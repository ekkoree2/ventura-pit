package eu.ventura.model;

import eu.ventura.constants.Sounds;
import eu.ventura.constants.Status;
import eu.ventura.constants.Strings;
import eu.ventura.service.PerkService;
import eu.ventura.service.PlayerService;
import eu.ventura.util.LevelUtil;
import eu.ventura.util.PlayerUtil;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * author: ekkoree
 * created at: 12/26/2025
 */
public class PlayerModel {
    public final Player player;

    public Player lastAttacker;

    public int requiredXP = 15;
    public int level = 120;
    public int prestige = 0;
    public int xp = 0;
    public Status status = Status.IDLING;
    public int combatTime = 0;
    public int multiKills = 0;
    public long lastKillTime = 0;
    public int bounty = 0;
    public int streak = 0;

    @Getter
    private PlayerEffectsModel effectsModel;

    public double gold = 999999;
    @Getter
    private Strings.Language language = Strings.Language.POLISH;

    public Set<String> purchasedPerks = new HashSet<>();
    public Map<Integer, String> equippedPerks = new HashMap<>();

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
}
