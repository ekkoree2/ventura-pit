package eu.ventura.model;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import eu.ventura.util.MongoUtil;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.util.UUID;

/**
 * author: ekkoree
 * created at: 1/22/2026
 */
@Getter
public class BugModel {
    private final String id;
    private final UUID uuid;
    private final String name;
    private final String rankColor;
    private final String issue;
    @Setter
    private boolean approved;

    public BugModel(String id, UUID uuid, String name, String rankColor, String issue, boolean approved) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.rankColor = rankColor;
        this.issue = issue;
        this.approved = approved;
    }

    public void save() {
        if (!MongoUtil.connected) return;
        MongoCollection<Document> collection = MongoUtil.getCollection("bugs");
        if (collection == null) return;
        Document doc = new Document("_id", id)
                .append("uuid", uuid.toString())
                .append("name", name)
                .append("rankColor", rankColor)
                .append("issue", issue)
                .append("approved", approved);

        collection.replaceOne(new Document("_id", id), doc, new ReplaceOptions().upsert(true));
    }

    public static BugModel load(Document doc) {
        if (doc == null) return null;

        String id = doc.getString("_id");
        UUID uuid = UUID.fromString(doc.getString("uuid"));
        String name = doc.getString("name");
        String rankColor = doc.getString("rankColor");
        String issue = doc.getString("issue");
        boolean approved = doc.getBoolean("approved", false);

        return new BugModel(id, uuid, name, rankColor, issue, approved);
    }
}
