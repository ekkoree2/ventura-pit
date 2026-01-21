package eu.ventura.listener;

import eu.ventura.Pit;
import eu.ventura.constants.Sounds;
import eu.ventura.constants.Strings;
import eu.ventura.event.PitAssistEvent;
import eu.ventura.event.PitDamageEvent;
import eu.ventura.event.PitKillEvent;
import eu.ventura.model.DeathModel;
import eu.ventura.model.PlayerModel;
import eu.ventura.util.PlayerUtil;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * author: ekkoree
 * created at: 1/15/2025
 */
public class AssistListener implements Listener {
    private final Map<UUID, Map<UUID, Double>> assistMap = new HashMap<>();

    public void addAssist(Player damaged, Player damager, double damage) {
        UUID damagerUUID = damager.getUniqueId();
        UUID damagedUUID = damaged.getUniqueId();

        Map<UUID, Double> assists = assistMap.computeIfAbsent(damagedUUID, k -> new HashMap<>());
        assists.merge(damagerUUID, damage, Double::sum);

        new BukkitRunnable() {
            @Override
            public void run() {
                Map<UUID, Double> updatedAssists = assistMap.get(damagedUUID);
                if (updatedAssists != null) {
                    updatedAssists.remove(damagerUUID);
                }
            }
        }.runTaskLater(Pit.instance, 300);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAttack(PitDamageEvent event) {
        Player damaged = event.data.victim;
        Player damager = event.data.trueAttacker;

        if (damaged == null || damager == null) {
            return;
        }

        double damage = Math.min(damaged.getHealth(), event.getFinalDamage());
        addAssist(damaged, damager, damage);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDeath(PitKillEvent event) {
        DeathModel data = event.data;
        Player killer = data.trueAttacker;
        Player dead = data.victim;

        if (killer == null || dead == null) {
            return;
        }

        UUID deadUUID = dead.getUniqueId();
        UUID killerUUID = killer.getUniqueId();

        Map<UUID, Double> assists = assistMap.getOrDefault(deadUUID, Collections.emptyMap());

        Map<UUID, Double> sortedAssists = assists.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));

        double damage = 0.0;
        for (Map.Entry<UUID, Double> entry : sortedAssists.entrySet()) {
            UUID uuid = entry.getKey();
            if (!uuid.equals(deadUUID) && !uuid.equals(killerUUID)) {
                damage += entry.getValue();
            }
        }

        if (damage <= 0) {
            assistMap.remove(deadUUID);
            return;
        }

        for (Map.Entry<UUID, Double> entry : sortedAssists.entrySet()) {
            UUID playerUUID = entry.getKey();
            double contribution = entry.getValue();
            Player player = Bukkit.getPlayer(playerUUID);

            if (player == null || playerUUID.equals(deadUUID) || playerUUID.equals(killerUUID)) {
                continue;
            }

            double percentage = Math.min(contribution / damage, 0.99);
            if (percentage <= 0) {
                continue;
            }

            PitAssistEvent assistEvent = new PitAssistEvent(data, player, percentage);
            Bukkit.getPluginManager().callEvent(assistEvent);
        }

        assistMap.remove(deadUUID);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAssist(PitAssistEvent event) {
        Player player = event.assister;
        DeathModel data = event.deathModel;

        PlayerModel model = PlayerModel.getInstance(player);

        double gold = data.gold * event.percentage;
        int xp = (int) Math.round(data.xp * event.percentage);

        model.addGold(gold);
        model.addXp(xp);

        int percent = (int) Math.round(event.percentage * 100.0);

        String message = Strings.Formatted.ASSIST_MESSAGE.format(
                player,
                percent,
                PlayerUtil.getDisplayName(data.victim),
                eu.ventura.constants.NumberFormat.DEF.of(xp),
                eu.ventura.constants.NumberFormat.GOLD_KILL.of(gold)
        );

        Sounds.ASSIST.play(player);
        LegacyComponentSerializer serializer = LegacyComponentSerializer.legacySection();
        player.sendMessage(serializer.deserialize(message));
    }
}
