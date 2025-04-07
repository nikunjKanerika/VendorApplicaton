package com.kanerika.Vendor.dbClasses;

import com.google.gson.Gson;
import com.kanerika.Vendor.config.MongoProperties;
import com.kanerika.Vendor.dto.VendorRequest;
import com.mongodb.client.*;

import com.kanerika.Vendor.GeneralVendor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class MongoDB implements GeneralVendor {
    @Autowired
    private MongoProperties mongoProperties;

    @Override
    public String connect(VendorRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (!mongoProperties.getPort().equals(request.getConnectionParam().getPort())) {
                throw new RuntimeException("Unsupported MongoDB port: " + request.getConnectionParam().getPort());
            }
            String uri = "mongodb://" + request.getConnectionParam().getHost() + ":" + request.getConnectionParam().getPort();
            String dbName = mongoProperties.getDbName();
            String collectionName = mongoProperties.getCollectionName();

            List<Map<String, Object>> data = new ArrayList<>();
            Set<String> columns = new LinkedHashSet<>();

            MongoClient mongoClient = MongoClients.create(uri);
            MongoDatabase database = mongoClient.getDatabase(dbName);
            Document ping = new Document("ping", 1);
            database.runCommand(ping);
            MongoCollection<Document> collection = database.getCollection(collectionName);
            FindIterable<Document> documents = collection.find();

            for (Document doc : documents) {
                Map<String, Object> row = new HashMap<>();
                for (Map.Entry<String, Object> entry : doc.entrySet()) {
                    row.put(entry.getKey(), entry.getValue());
                    columns.add(entry.getKey());
                }
                data.add(row);
            }

            response.put("status", "success");
            response.put("message", "Connected to MongoDB successfully!");
            response.put("columns", new ArrayList<>(columns));
            response.put("data", data);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Connection to MongoDB failed");
            response.put("error", e.getMessage());
        }

        return new Gson().toJson(response);
    }


}
