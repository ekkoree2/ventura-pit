package eu.ventura.listener;

import eu.ventura.Pit;
import eu.ventura.constants.*;
import eu.ventura.event.PitDeathEvent;
import eu.ventura.event.PitKillEvent;
import eu.ventura.event.PitRespawnEvent;
import eu.ventura.maps.Map;
import eu.ventura.model.DeathModel;
import eu.ventura.model.PlayerModel;
import eu.ventura.service.MapService;
import eu.ventura.util.EnchantmentHelper;
import eu.ventura.util.EquipmentUtil;
import eu.ventura.util.PlayerUtil;
import hvh.ventura.hologram.Hologram;
import hvh.ventura.npc.NPC;
import hvh.ventura.npc.api.NpcApi;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.UUID;

/**
* author: ekkoree
* created at: 1/15/2025
  */
@RequiredArgsConstructor
public class PlayerListener implements Listener {
    private final NpcApi npcApi;
    private final hvh.ventura.hologram.api.HologramApi hologramApi;

    private final HashMap<String, Hologram> holograms = new HashMap<>();
    private final HashMap<String, NPC> npcs = new HashMap<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(PitDeathEvent event) {
        Player victim = event.data.victim;
        String message;
        if (event.data.trueAttacker != null) {
            message = Strings.Formatted.DEATH_MESSAGE.format(victim, PlayerUtil.getDisplayName(event.data.trueAttacker));
        } else {
            message = Strings.Simple.DEATH.get(victim);
        }
        LegacyComponentSerializer serializer = LegacyComponentSerializer.legacySection();
        victim.sendMessage(serializer.deserialize(message));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setFoodLevel(19);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onKill(PitKillEvent event) {
        Player attacker = event.data.trueAttacker;
        DeathModel data = event.data;

        PlayerModel playerModel = PlayerModel.getInstance(attacker);
        playerModel.addKill();
        playerModel.addXp(data.xp);
        playerModel.addGold(data.gold);

        playerModel.streak += 1;

        String message = Strings.Formatted.KILL_MESSAGE.format(
                attacker,
                getKillPrefix(attacker),
                PlayerUtil.getDisplayName(data.victim),
                NumberFormat.DEF.of(data.xp),
                NumberFormat.GOLD_KILL.of(data.gold)
        );

        getKillSound(event.data.trueAttacker).play(event.data.trueAttacker);

        Bukkit.getScheduler().runTask(Pit.getInstance(), () -> {
            PlayerUtil.displayIndicator(event.data.trueAttacker, Strings.Formatted.KILL_TITLE.format(
                    event.data.trueAttacker,
                    PlayerUtil.getDisplayName(event.data.victim)
            ));
        });

        LegacyComponentSerializer serializer = LegacyComponentSerializer.legacySection();
        attacker.sendMessage(serializer.deserialize(message));

        PitRespawnEvent pitRespawnEvent = new PitRespawnEvent(event.data.victim, RespawnReason.DEATH);
        Bukkit.getPluginManager().callEvent(pitRespawnEvent);
    }

    private String getKillPrefix(Player player) {
        PlayerModel playerModel = PlayerModel.getInstance(player);
        int multiKills = playerModel.getMultiKillsNumber();
        return switch (multiKills) {
            case 1 -> Strings.Simple.KILL_1.get(player);
            case 2 -> Strings.Simple.KILL_2.get(player);
            case 3 -> Strings.Simple.KILL_3.get(player);
            case 4 -> Strings.Simple.KILL_4.get(player);
            case 5 -> Strings.Simple.KILL_5.get(player);
            default -> Strings.Simple.KILL_MULTI.get(player).replace("{0}", String.valueOf(multiKills));
        };
    }

    private Sounds.SoundEffect getKillSound(Player player) {
        PlayerModel playerModel = PlayerModel.getInstance(player);
        int multiKills = playerModel.getMultiKillsNumber();
        return switch (multiKills) {
            case 1 -> Sounds.KILL;
            case 2 -> Sounds.DOUBLE_KILL;
            case 3 -> Sounds.TRIPLE_KILL;
            case 4 -> Sounds.QUADRA_KILL;
            default -> Sounds.PENTA_KILL;
        };
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        PitRespawnEvent pitRespawnEvent = new PitRespawnEvent(event.getPlayer(), RespawnReason.JOIN);
        Bukkit.getPluginManager().callEvent(pitRespawnEvent);
    }

    @EventHandler
    public void onRespawn(PitRespawnEvent event) {
        Player player = event.getPlayer();

        Location location = MapService.getRandomSpawnLocation().of();
        player.teleport(location);

        PlayerModel model = PlayerModel.getInstance(player);
        model.streak = 0;

        player.setFoodLevel(19);
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));

        if (event.getReason() == RespawnReason.DEATH || event.getReason() == RespawnReason.MEGASTREAK_DEATH) {
            Sounds.DEATH.play(player);
        }

        if (event.getReason() == RespawnReason.JOIN) {
            for (Map.Hologram hologram : Pit.getMap().getInstance().getHolograms()) {
                String hologramName = hologram.getPitHologram().name();

                if (!holograms.containsKey(hologramName)) {
                    Location loc = hologram.getLocation().of();
                    String[] lines = hologram.getPitHologram().getLines(player).toArray(new String[0]);
                    holograms.put(hologramName, hologramApi.createHologram(loc, lines));
                }
                holograms.get(hologramName).spawn(player);
            }

            for (Map.Npc npc : Pit.getMap().getInstance().getNpcs()) {
                Location npcLocation = npc.getLocation().of();
                String npcName = npc.getPitNpc().getSkin();
                String skin = npc.getPitNpc().getSkin();
                UUID npcUuid = UUID.nameUUIDFromBytes(npcName.getBytes());

                if (!npcs.containsKey(npcName)) {
                    NPC created = npcApi.createNPC("", npcLocation, npcUuid, skin, null);
                    if (npc.getPitNpc().getTask() != null) {
                        created.setInteractAction(p -> npc.getPitNpc().getTask().accept(p));
                    }
                    npcs.put(npcName, created);
                }
                npcs.get(npcName).spawn(player);
                npcs.get(npcName).setPitch(0);
                npcs.get(npcName).setYaw(npc.getPitNpc().getYaw());

                PitHologram pitHologram = npc.getPitNpc().getHologram();
                String holoName = npcName + "_" + player.getUniqueId();
                Location holoLoc = npcLocation.clone().add(0, 2.1, 0);

                if (!holograms.containsKey(holoName)) {
                    String[] lines = pitHologram.getLines(player).toArray(new String[0]);
                    holograms.put(holoName, hologramApi.createHologram(holoLoc, lines));
                }
                holograms.get(holoName).spawn(player);
            }
        }

        player.setHealth(player.getMaxHealth());

        EquipmentUtil.giveDefaultGear(player);
        EnchantmentHelper.syncInventory(player);
    }
}
