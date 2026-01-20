package eu.ventura.util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * author: ekkoree
 * created at: 1/20/2026
 */
public class MongoUtil {
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    public static void initialize(String connectionString, String dbName) {
        mongoClient = MongoClients.create(connectionString);
        database = mongoClient.getDatabase(dbName);
    }

    public static MongoCollection<Document> getCollection(String collectionName) {
        return database.getCollection(collectionName);
    }

    public static void shutdown() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
