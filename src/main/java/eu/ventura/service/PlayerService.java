package eu.ventura.service;

import eu.ventura.model.PlayerModel;
import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentHashMap;

/**
 * author: ekkoree
 * created at: 12/26/2025
 */
public class PlayerService {
    public static final ConcurrentHashMap<Player, PlayerModel> players = new ConcurrentHashMap<>();

    public static PlayerModel getPlayer(Player player) {
        return players.computeIfAbsent(player, p -> {
            PlayerModel model = new PlayerModel(p);
            model.load();
            return model;
        });
    }

    public static void removePlayer(Player player) {
        PlayerModel model = players.remove(player);
        if (model != null) {
            model.save();
        }
    }

    public static void saveAll() {
        players.values().forEach(PlayerModel::save);
    }
}
