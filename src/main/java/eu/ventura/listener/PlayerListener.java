package eu.ventura.listener;

import eu.ventura.Pit;
import eu.ventura.constants.*;
import eu.ventura.event.PitDeathEvent;
import eu.ventura.event.PitKillEvent;
import eu.ventura.event.PitRespawnEvent;
import eu.ventura.events.major.impl.RagePit;
import eu.ventura.maps.Map;
import eu.ventura.model.DeathModel;
import eu.ventura.model.PlayerModel;
import eu.ventura.service.BossBarService;
import eu.ventura.service.LeaderboardService;
import eu.ventura.service.MapService;
import eu.ventura.service.PlayerService;
import eu.ventura.util.EnchantmentHelper;
import eu.ventura.util.EquipmentUtil;
import eu.ventura.util.LevelUtil;
import eu.ventura.util.NBTHelper;
import eu.ventura.util.NBTTag;
import eu.ventura.util.PlayerUtil;
import hvh.ventura.hologram.Hologram;
import hvh.ventura.npc.NPC;
import hvh.ventura.npc.api.NpcApi;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
* author: ekkoree
* created at: 1/15/2025
  */
@SuppressWarnings({"unused", "deprecation", "BooleanMethodIsAlwaysInverted"})
@RequiredArgsConstructor
public class PlayerListener implements Listener {
    private final NpcApi npcApi;
    private final hvh.ventura.hologram.api.HologramApi hologramApi;

    private final HashMap<String, Hologram> holograms = new HashMap<>();
    private final HashMap<String, NPC> npcs = new HashMap<>();
    private final HashMap<Player, BukkitTask> nametagTasks = new HashMap<>();

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

        Bukkit.getPluginManager().callEvent(new PitRespawnEvent(victim, RespawnReason.DEATH));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setFoodLevel(19);
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        if (event.getItem().getType() != Material.GOLDEN_APPLE) {
            return;
        }

        Player player = event.getPlayer();
        Bukkit.getScheduler().runTask(Pit.instance, () -> {
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 80, 1));

            double abs = player.getAbsorptionAmount();
            double newAbs = Math.min(abs + 6, 10);
            if (newAbs > abs) {
                PlayerUtil.setAbs(player, newAbs);
            }
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onKill(PitKillEvent event) {
        Player attacker = event.data.trueAttacker;
        DeathModel data = event.data;

        PlayerModel playerModel = PlayerModel.getInstance(attacker);
        playerModel.addKill();

        int xp = data.getFinalXp();
        double gold = data.getFinalGold();

        playerModel.addXp(xp);
        playerModel.addGold(gold);

        if (Pit.event == null) {
            playerModel.streak += 1;
        }

        String message = Strings.Formatted.KILL_MESSAGE.format(
                attacker,
                getKillPrefix(attacker),
                PlayerUtil.getDisplayName(data.victim),
                NumberFormat.DEF.of(xp),
                NumberFormat.GOLD_KILL.of(gold)
        );

        getKillSound(event.data.trueAttacker).play(event.data.trueAttacker);

        Bukkit.getScheduler().runTask(Pit.instance, () -> PlayerUtil.displayIndicator(event.data.trueAttacker, Strings.Formatted.KILL_TITLE.format(
                event.data.trueAttacker,
                PlayerUtil.getDisplayName(event.data.victim)
        )));

        LegacyComponentSerializer serializer = LegacyComponentSerializer.legacySection();
        attacker.sendMessage(serializer.deserialize(message));

        if (!playerModel.hasHealingPerk()) {
            if (!(Pit.event instanceof RagePit)) {
                int apples = EquipmentUtil.getAppleCount(attacker.getInventory());
                if (apples < 2) {
                    attacker.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 1));
                }
            }
        }

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
        event.setJoinMessage(null);
        Player player = event.getPlayer();
        setupNameTag(player);
        PitRespawnEvent pitRespawnEvent = new PitRespawnEvent(player, RespawnReason.JOIN);
        Bukkit.getPluginManager().callEvent(pitRespawnEvent);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        BukkitTask task = nametagTasks.get(player);
        if (task != null) {
            task.cancel();
            nametagTasks.remove(player);
        }

        List<Hologram> toDestroy = new ArrayList<>();
        List<String> toRemove = new ArrayList<>();

        for (Map.Npc npc : Pit.map.getInstance().getNpcs()) {
            String npcName = npc.getPitNpc().getSkin();
            String holoName = npcName + "_" + playerId;

            NPC npcInstance = npcs.get(npcName);
            if (npcInstance != null) {
                npcInstance.despawn(player);
            }

            Hologram hologram = holograms.get(holoName);
            if (hologram != null) {
                toDestroy.add(hologram);
                toRemove.add(holoName);
            }
        }

        Bukkit.getScheduler().runTask(Pit.instance, () -> {
            for (Hologram hologram : toDestroy) {
                hologramApi.destroy(hologram);
            }
            toRemove.forEach(holograms::remove);
        });

        BossBarService.getInstance().cleanup(player);
        LeaderboardService.getInstance(hologramApi, Pit.map.getInstance().getLeaderboardLocation().of()).removeHologram(player);
        PlayerService.removePlayer(player);
    }

    @EventHandler
    public void onRespawn(PitRespawnEvent event) {
        Player player = event.getPlayer();

        Location location = MapService.getRandomSpawnLocation().of();
        player.teleport(location);

        PlayerModel model = PlayerModel.getInstance(player);
        if (Pit.event == null) {
            model.streak = 0;
        }
        model.lastAttacker = null;
        model.status = Status.IDLING;

        PlayerUtil.updateMaxHealth(player, true);
        player.setFoodLevel(19);

        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        player.setAbsorptionAmount(0);
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));

        if (event.getReason() == RespawnReason.DEATH || event.getReason() == RespawnReason.MEGASTREAK_DEATH) {
            Sounds.DEATH.play(player);
        }

        model.clearItemsByTag(NBTTag.PERK_ITEM);
        model.clearItemsByTag(NBTTag.EVENT_ITEM);
        model.clearItemsByTag(NBTTag.RESTRICTED_ITEM);

        if (event.getReason() == RespawnReason.JOIN) {
            LeaderboardService.getInstance(hologramApi, Pit.map.getInstance().getLeaderboardLocation().of()).showHologram(player);

            for (Map.Hologram hologram : Pit.map.getInstance().getHolograms()) {
                String hologramName = hologram.getPitHologram().name();

                if (!holograms.containsKey(hologramName)) {
                    Location loc = hologram.getLocation().of();
                    String[] lines = hologram.getPitHologram().getLines(player).toArray(new String[0]);
                    Hologram created = hologramApi.createHologram(loc, lines);
                    created.spawn();
                    holograms.put(hologramName, created);
                }
                holograms.get(hologramName).show(player);
            }

            for (Map.Npc npc : Pit.map.getInstance().getNpcs()) {
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
                    Hologram created = hologramApi.createHologram(holoLoc, player.getUniqueId(), lines);
                    created.spawn();
                    holograms.put(holoName, created);
                }
                holograms.get(holoName).show(player);
            }
        }

        if (event.getReason() == RespawnReason.DEATH || event.getReason() == RespawnReason.MEGASTREAK_DEATH) {
            clearShopItems(player);
        }

        EquipmentUtil.giveDefaultGear(player);
        EnchantmentHelper.syncInventory(player);
    }

    private void clearShopItems(Player player) {
        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item == null || item.getType() == Material.AIR) {
                continue;
            }
            if (!shouldKeepItem(item)) {
                player.getInventory().setItem(i, new ItemStack(Material.AIR));
            }
        }

        ItemStack[] armor = player.getInventory().getArmorContents();
        for (int i = 0; i < armor.length; i++) {
            ItemStack item = armor[i];
            if (item == null || item.getType() == Material.AIR) {
                continue;
            }
            if (!shouldKeepItem(item)) {
                armor[i] = new ItemStack(Material.AIR);
            }
        }
        player.getInventory().setArmorContents(armor);
    }

    private boolean shouldKeepItem(ItemStack item) {
        return NBTHelper.getBoolean(item, NBTTag.DEFAULT_ITEM.getValue())
                || NBTHelper.hasKey(item, NBTTag.MYSTIC_ITEM.getValue());
    }

    private void setupNameTag(Player player) {
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(Pit.instance, () -> {
            Scoreboard scoreboard = player.getScoreboard();

            for (Player target : player.getWorld().getPlayers()) {
                PlayerModel playerModel = PlayerModel.getInstance(target);
                String prefix = LevelUtil.getFormattedLevelFromValuesChat(playerModel);
                int level = playerModel.getLevel();

                String paddedLevel = String.format("%03d", 999 - level);
                String teamName = paddedLevel + target.getUniqueId().toString().substring(0, 8);

                Team team = scoreboard.getTeam(teamName);
                if (team == null) {
                    team = scoreboard.registerNewTeam(teamName);
                }

                String suffix = "";
                if (playerModel.getBounty() > 0 && Pit.event == null) {
                    suffix += "§6§l" + playerModel.getBounty() + "g";
                }

                team.setPrefix(ChatColor.translateAlternateColorCodes('&', prefix + PlayerUtil.getRankColor(target) + " "));
                team.setColor(PlayerUtil.getRankColorChat(target));
                team.setSuffix(" " + suffix);

                if (!team.hasEntry(target.getName())) {
                    team.addEntry(target.getName());
                }
            }

            player.setPlayerListHeader(Strings.Simple.HEADER.get(player));
            player.setPlayerListFooter(Strings.Simple.FOOTER.get(player));
        }, 0, 20);
        nametagTasks.put(player, task);
    }
}
