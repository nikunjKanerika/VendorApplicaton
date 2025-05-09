package com.kanerika.Vendor.controller;
import com.kanerika.Vendor.GeneralVendor;
import com.kanerika.Vendor.dbClasses.MongoDB;
import com.kanerika.Vendor.dbClasses.Postgres;
import com.kanerika.Vendor.dbClasses.MySQL;
import com.kanerika.Vendor.dto.VendorRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

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
        String vendorName = request.getVendor();
        String result;
        GeneralVendor db;

        switch (vendorName.toLowerCase()) {
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
                throw new IllegalArgumentException("Unsupported vendor: " + vendorName);
        }

        logger.info("Host: " + request.getConnectionParam().getHost());
        logger.info("Port: " + request.getConnectionParam().getPort());
        logger.info("Schema path: " + request.getSchemapath());

        result = db.connect(request);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return ResponseEntity.ok()
                .headers(headers)
                .body(result);
    }

}
