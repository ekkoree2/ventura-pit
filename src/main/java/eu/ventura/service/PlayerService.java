package eu.ventura.service;

import eu.ventura.model.PlayerModel;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentHashMap;

/**
 * author: ekkoree
 * created at: 12/26/2025
 */
public class PlayerService {
    public static final ConcurrentHashMap<Player, PlayerModel> players = new ConcurrentHashMap<>();

    public static PlayerModel getPlayer(Player player) {
        return players.computeIfAbsent(player, PlayerModel::new);
    }
}
