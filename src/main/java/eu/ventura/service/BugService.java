package eu.ventura.service;

import com.mongodb.client.MongoCollection;
import eu.ventura.model.BugModel;
import eu.ventura.util.MongoUtil;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * author: ekkoree
 * created at: 1/22/2026
 */
public class BugService {
    private static final Map<Integer, BugModel> bugs = new ConcurrentHashMap<>();

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

    public static int getNextId() {
        return bugs.isEmpty() ? 1 : bugs.keySet().stream().max(Integer::compareTo).orElse(0) + 1;
    }

    public static void addBug(BugModel bug) {
        bugs.put(bug.getId(), bug);
        bug.save();
    }

    public static BugModel getBug(int id) {
        return bugs.get(id);
    }

    public static List<BugModel> getAllBugs() {
        return new ArrayList<>(bugs.values());
    }

    public static int getCount() {
        return bugs.size();
    }
}
