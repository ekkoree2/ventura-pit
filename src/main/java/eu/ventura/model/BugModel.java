package eu.ventura.model;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import eu.ventura.util.MongoUtil;
import lombok.Getter;
import org.bson.Document;

import java.util.UUID;

/**
 * author: ekkoree
 * created at: 1/22/2026
 */
@Getter
public class BugModel {
    private final int id;
    private final UUID uuid;
    private final String name;
    private final String rankColor;
    private final String issue;

    public BugModel(int id, UUID uuid, String name, String rankColor, String issue) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.rankColor = rankColor;
        this.issue = issue;
    }

    public void save() {
        MongoCollection<Document> collection = MongoUtil.getCollection("bugs");
        Document doc = new Document("_id", id)
                .append("uuid", uuid.toString())
                .append("name", name)
                .append("rankColor", rankColor)
                .append("issue", issue);

        collection.replaceOne(new Document("_id", id), doc, new ReplaceOptions().upsert(true));
    }

    public static BugModel load(Document doc) {
        if (doc == null) return null;

        int id = doc.getInteger("_id");
        UUID uuid = UUID.fromString(doc.getString("uuid"));
        String name = doc.getString("name");
        String rankColor = doc.getString("rankColor");
        String issue = doc.getString("issue");

        return new BugModel(id, uuid, name, rankColor, issue);
    }
}
