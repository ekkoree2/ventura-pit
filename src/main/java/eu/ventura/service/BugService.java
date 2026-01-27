package eu.ventura.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import eu.ventura.model.BugModel;
import eu.ventura.util.MongoUtil;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * author: ekkoree
 * created at: 1/22/2026
 */
public class BugService {
    private static final Map<String, BugModel> bugs = new ConcurrentHashMap<>();
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final Random random = new Random();

    public static void loadAll() {
        bugs.clear();
        MongoCollection<Document> collection = MongoUtil.getCollection("bugs");
        for (Document doc : collection.find()) {
            BugModel bug = BugModel.load(doc);
            if (bug != null) {
                bugs.put(bug.getId(), bug);
            }
        }
    }

    public static String generateId() {
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        String id = sb.toString();
        return bugs.containsKey(id) ? generateId() : id;
    }

    public static void addBug(BugModel bug) {
        bugs.put(bug.getId(), bug);
        bug.save();
    }

    public static BugModel getBug(String id) {
        return bugs.get(id);
    }

    public static List<BugModel> getAllBugs() {
        return new ArrayList<>(bugs.values());
    }

    public static int getCount() {
        return bugs.size();
    }

    public static void removeBug(String id) {
        bugs.remove(id);
        MongoCollection<Document> collection = MongoUtil.getCollection("bugs");
        collection.deleteOne(new Document("_id", id));
    }

    public static void addPendingReward(String uuid, String bugId, int xp, double gold) {
        MongoCollection<Document> collection = MongoUtil.getCollection("pending_bug_rewards");
        Document doc = new Document("_id", uuid)
                .append("bugId", bugId)
                .append("xp", xp)
                .append("gold", gold);
        collection.replaceOne(new Document("_id", uuid), doc, new ReplaceOptions().upsert(true));
    }

    public static Document getPendingReward(String uuid) {
        MongoCollection<Document> collection = MongoUtil.getCollection("pending_bug_rewards");
        return collection.find(new Document("_id", uuid)).first();
    }

    public static void removePendingReward(String uuid) {
        MongoCollection<Document> collection = MongoUtil.getCollection("pending_bug_rewards");
        collection.deleteOne(new Document("_id", uuid));
    }
}
