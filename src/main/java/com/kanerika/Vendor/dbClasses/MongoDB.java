//package com.example.Vendor.dbClasses;
//
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//import com.mongodb.client.MongoDatabase;
//import com.example.Vendor.GeneralVendor;
//
//public class MongoDB implements GeneralVendor {
//
//    @Override
//    public String connect() {
//        String connectionString = "mongodb://localhost:27017"; // replace with dynamic param if needed
//
//        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
//            MongoDatabase database = mongoClient.getDatabase("test"); // replace with your DB name
//            return "Connected to MongoDB. Database name: " + database.getName();
//        } catch (Exception e) {
//            return "Connection to MongoDB failed: " + e.getMessage();
//        }
//    }
//}
package com.kanerika.Vendor.dbClasses;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.FindIterable;

import com.kanerika.Vendor.GeneralVendor;
import org.bson.Document;

public class MongoDB implements GeneralVendor {

    @Override
    public String connect() {
        String connectionString = "mongodb://localhost:27017"; // replace with dynamic param if needed

        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoDatabase database = mongoClient.getDatabase("test"); // replace with your DB name
            MongoCollection<Document> collection = database.getCollection("employees"); // your collection name

            StringBuilder result = new StringBuilder("Connected to MongoDB. Database name: " + database.getName() + "\n");
//            result.append("Data in 'employees' collection:\n");

            // Retrieve and iterate over documents
            FindIterable<Document> documents = collection.find();
            for (Document doc : documents) {
                result.append(doc.toJson()).append("\n");
            }

            return result.toString();
        } catch (Exception e) {
            return "Connection to MongoDB failed: " + e.getMessage();
        }
    }
}
