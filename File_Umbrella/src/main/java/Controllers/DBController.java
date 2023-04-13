package Controllers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class DBController {
    // create a method to store the mongodb connection string
    public static MongoClient createConnection(String connectionString) {
        return MongoClients.create(connectionString);
    }

    public static String closeConnection() {
        return "Connection closed";
    }
}
