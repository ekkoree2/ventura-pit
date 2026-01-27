package eu.ventura.service;

import eu.ventura.model.PlayerModel;
import eu.ventura.util.MongoUtil;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * author: ekkoree
 * created at: 12/26/2025
 */
public class PlayerService {
    public static final ConcurrentHashMap<UUID, PlayerModel> players = new ConcurrentHashMap<>();

    public static PlayerModel getPlayer(Player player) {
        return players.computeIfAbsent(player.getUniqueId(), uuid -> {
            PlayerModel model = new PlayerModel(player);
            model.load();
            return model;
        });
    }

    public static void removePlayer(Player player) {
        PlayerModel model = players.remove(player.getUniqueId());
        if (model != null) {
            model.save();
        }
    }

    public static void saveAll() {
        players.values().forEach(PlayerModel::save);
    }

    public static void wipe(Player player) {
        players.remove(player.getUniqueId());
        MongoUtil.getCollection("players").deleteOne(new Document("_id", player.getUniqueId().toString()));
        player.kick();
    }

    public static void wipe(String username) {
        Document doc = MongoUtil.getCollection("players").find(new Document("username", username)).first();
        if (doc == null) {
            return;
        }
        String uuidString = doc.getString("_id");
        UUID uuid = UUID.fromString(uuidString);

        players.remove(uuid);
        MongoUtil.getCollection("players").deleteOne(new Document("_id", uuidString));

        Player player = Bukkit.getPlayer(uuid);
        if (player != null && player.isOnline()) {
            player.kick();
        }
    }

    public static List<String> getAllUsernames() {
        List<String> usernames = new ArrayList<>();
        for (Document doc : MongoUtil.getCollection("players").find()) {
            String username = doc.getString("username");
            if (username != null) {
                usernames.add(username);
            }
        }
        return usernames;
    }

    public static boolean move(String fromUsername, String toUsername) {
        try {
            Document fromDoc = MongoUtil.getCollection("players").find(new Document("username", fromUsername)).first();
            if (fromDoc == null) {
                System.out.println("[PlayerService] Source player not found: " + fromUsername);
                return false;
            }

            Document toDoc = MongoUtil.getCollection("players").find(new Document("username", toUsername)).first();
            if (toDoc == null) {
                System.out.println("[PlayerService] Target player not found: " + toUsername);
                return false;
            }

            String fromUuid = fromDoc.getString("_id");
            String toUuid = toDoc.getString("_id");
            UUID fromPlayerUuid = UUID.fromString(fromUuid);
            UUID toPlayerUuid = UUID.fromString(toUuid);

            PlayerModel fromModel = players.get(fromPlayerUuid);
            PlayerModel toModel = players.get(toPlayerUuid);

            if (fromModel != null) {
                fromModel.save();
            }
            if (toModel != null) {
                toModel.save();
            }

            fromDoc = MongoUtil.getCollection("players").find(new Document("username", fromUsername)).first();
            toDoc = MongoUtil.getCollection("players").find(new Document("username", toUsername)).first();

            Document newFromDoc = new Document("_id", fromUuid);
            Document newToDoc = new Document("_id", toUuid);

            for (String key : toDoc.keySet()) {
                if (!key.equals("_id") && !key.equals("username") && !key.equals("rankColor")) {
                    newFromDoc.append(key, toDoc.get(key));
                }
            }
            newFromDoc.append("username", fromUsername);
            newFromDoc.append("rankColor", fromDoc.get("rankColor"));

            for (String key : fromDoc.keySet()) {
                if (!key.equals("_id") && !key.equals("username") && !key.equals("rankColor")) {
                    newToDoc.append(key, fromDoc.get(key));
                }
            }
            newToDoc.append("username", toUsername);
            newToDoc.append("rankColor", toDoc.get("rankColor"));

            MongoUtil.getCollection("players").replaceOne(new Document("_id", fromUuid), newFromDoc);
            MongoUtil.getCollection("players").replaceOne(new Document("_id", toUuid), newToDoc);

            players.remove(fromPlayerUuid);
            players.remove(toPlayerUuid);

            Player fromPlayer = Bukkit.getPlayer(fromPlayerUuid);
            Player toPlayer = Bukkit.getPlayer(toPlayerUuid);

            if (fromPlayer != null && fromPlayer.isOnline()) {
                fromPlayer.kick();
            }
            if (toPlayer != null && toPlayer.isOnline()) {
                toPlayer.kick();
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
