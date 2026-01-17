package eu.ventura.listener;

import de.oliver.fancyholograms.api.FancyHologramsPlugin;
import de.oliver.fancyholograms.api.HologramManager;
import de.oliver.fancyholograms.api.data.TextHologramData;
import de.oliver.fancyholograms.api.hologram.Hologram;
import de.oliver.fancynpcs.api.FancyNpcsPlugin;
import de.oliver.fancynpcs.api.Npc;
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

/**
 * author: ekkoree
 * created at: 1/15/2025
 */
public class PlayerListener implements Listener {
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
            HologramManager hologramManager = FancyHologramsPlugin.get().getHologramManager();
            for (Map.Hologram hologram : Pit.getMap().getInstance().getHolograms()) {
                String hologramName = hologram.getPitHologram().name();
                hologramManager.getHologram(hologramName).ifPresentOrElse(
                        h -> h.forceShowHologram(player),
                        () -> {
                            TextHologramData data = new TextHologramData(hologramName, hologram.getLocation().of());

                            data.setText(hologram.getPitHologram().getLines(player));
                            data.setBackground(null);

                            Hologram created = hologramManager.create(data);
                            created.forceShowHologram(player);
                        }
                );
            }

            for (Map.Npc npc : Pit.getMap().getInstance().getNpcs()) {
                Location npcLocation = npc.getLocation().of();
                Npc bot = FancyNpcsPlugin.get().getNpcAdapter().apply(npc.getPitNpc().getNpcData(player, npcLocation));
                bot.setSaveToFile(false);

                FancyNpcsPlugin.get().getNpcManager().registerNpc(bot);
                bot.create();

                FancyNpcsPlugin.get().getNpcThread().submit(() -> {
                    bot.spawn(player);

                    PitHologram hologram = npc.getPitNpc().getHologram();
                    String name = npc.getPitNpc().name() + "_" + player.getUniqueId();
                    Location loc = npcLocation.clone().add(0, 1.9, 0);

                    hologramManager.getHologram(name).ifPresentOrElse(
                            h -> h.forceShowHologram(player),
                            () -> {
                                TextHologramData data = new TextHologramData(name, loc);

                                data.setText(hologram.getLines(player));
                                data.setBackground(null);

                                Hologram created = hologramManager.create(data);
                                created.forceShowHologram(player);
                            }
                    );
                });
            }
        }

        player.setHealth(player.getMaxHealth());

        EquipmentUtil.giveDefaultGear(player);
        EnchantmentHelper.syncInventory(player);
    }
}
