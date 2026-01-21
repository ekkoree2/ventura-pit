package eu.ventura.util;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import org.bson.Document;

import java.util.concurrent.TimeUnit;

/**
 * author: ekkoree
 * created at: 1/20/2026
 */
public class MongoUtil {
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static boolean connected = false;

    public static void initialize(String connectionString, String dbName) {
        try {
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(connectionString))
                    .applyToSocketSettings(builder -> builder
                            .connectTimeout(5, TimeUnit.SECONDS)
                            .readTimeout(10, TimeUnit.SECONDS))
                    .applyToServerSettings(builder -> builder
                            .heartbeatFrequency(10, TimeUnit.SECONDS)
                            .minHeartbeatFrequency(5, TimeUnit.SECONDS))
                    .applyToClusterSettings(builder -> builder
                            .serverSelectionTimeout(5, TimeUnit.SECONDS))
                    .build();

            mongoClient = MongoClients.create(settings);
            database = mongoClient.getDatabase(dbName);
            database.runCommand(new Document("ping", 1));
            connected = true;
        } catch (MongoException e) {
            connected = false;
            System.err.println("[MongoUtil] Failed to connect to MongoDB: " + e.getMessage());
        }
    }

    public static MongoCollection<Document> getCollection(String collectionName) {
        return database.getCollection(collectionName);
    }

    public static boolean isConnected() {
        return connected;
    }

    public static void shutdown() {
        if (mongoClient != null) {
            mongoClient.close();
        }
        connected = false;
    }
}
