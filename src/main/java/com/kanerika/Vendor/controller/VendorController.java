package com.kanerika.Vendor.controller;
import com.kanerika.Vendor.GeneralVendor;
import com.kanerika.Vendor.dbClasses.MongoDB;
import com.kanerika.Vendor.dbClasses.Postgres;
import com.kanerika.Vendor.dbClasses.MySQL;
import com.kanerika.Vendor.dto.ConnectionParam;
import com.kanerika.Vendor.dto.VendorRequest;
import com.kanerika.Vendor.util.JdbcConnectionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.sql.SQLException;

import static com.kanerika.Vendor.util.JdbcConnectionHelper.getHostAndPortByConnectionId;

@RestController
@RequestMapping("/vendor")
public class VendorController {

    private static final Logger logger = LoggerFactory.getLogger(VendorController.class.getSimpleName());

    private final MongoDB mongoDB;
    private final Postgres postgres;
    private final MySQL mySQL;

    @Autowired
    public VendorController(MongoDB mongoDB, Postgres postgres, MySQL mySQL) {
        this.mongoDB = mongoDB;
        this.postgres = postgres;
        this.mySQL = mySQL;
    }

    @PostMapping
    public ResponseEntity<String> validate(@RequestBody VendorRequest request) {
//        String vendorName = request.getVendor();
        String vendor = "";
        String connectionId = request.getConnectionId();
        try {
            // Call the static method from JdbcConnectionHelper
             vendor = JdbcConnectionHelper.getVendorByConnectionId(connectionId);

            // Print the result
            System.out.println("Vendor for connectionId " + connectionId + ": " + vendor);
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }

        String result;
        GeneralVendor db;

        switch (vendor.toLowerCase()) {
            case "postgres":
                db = postgres;
                break;
            case "mongodb":
                db = mongoDB;
                break;
            case "mysql":
                db = mySQL;
                break;
            default:
                throw new IllegalArgumentException("Unsupported vendor: " + vendor);
        }

        result = db.connect(request);

        logger.info("Schema path: " + request.getSchemapath());
        String tableName = request.getTable();

        try {
            String[] hostAndPort = JdbcConnectionHelper.getHostAndPortByConnectionId(connectionId);
            String[] usernameAndPassword = JdbcConnectionHelper.getUsernameAndPassword(connectionId);

            result = db.connect(hostAndPort[0], hostAndPort[1], usernameAndPassword[0], usernameAndPassword[1]);
        } catch (Exception e) {
            logger.error("❌ Error while connecting to DB: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Error occurred while connecting to database: " + e.getMessage());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return ResponseEntity.ok()
                .headers(headers)
                .body(result);
    }

}
