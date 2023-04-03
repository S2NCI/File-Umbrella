package Controllers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class DBController {
    // create a method to store the mongodb connection string
    public static MongoClient createConnection(String connectionString) {
        MongoClient mongoClient = MongoClients.create(connectionString);
        return mongoClient;
    }
}
