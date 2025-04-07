package com.kanerika.Vendor.controller;
import com.kanerika.Vendor.GeneralVendor;
import com.kanerika.Vendor.dbClasses.MongoDB;
import com.kanerika.Vendor.dbClasses.Postgres;
import com.kanerika.Vendor.dbClasses.MySQL;
import com.kanerika.Vendor.dto.VendorRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/vendor")
public class VendorController {

    @PostMapping
    public ResponseEntity<String> validate(@RequestBody VendorRequest request) {
        String vendorName = request.getVendor();
        String result;
        GeneralVendor db;

        switch (vendorName.toLowerCase()) {
            case "postgres":
                db = new Postgres();
                break;
            case "mongodb":
                db = new MongoDB();
                break;
            case "mysql":
                db = new MySQL();
                break;
            default:
                throw new IllegalArgumentException("Unsupported vendor: " + vendorName);
        }

        System.out.println("Host: " + request.getConnectionParam().get("host"));
        System.out.println("Port: " + request.getConnectionParam().get("port"));
        System.out.println("Schema path: " + request.getSchemapath());

        result = db.connect();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return ResponseEntity.ok()
                .headers(headers)
                .body(result);
    }

}
