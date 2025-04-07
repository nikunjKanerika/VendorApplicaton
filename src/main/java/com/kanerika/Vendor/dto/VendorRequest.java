// src/main/java/com/example/Vendor/dto/VendorRequest.java
package com.kanerika.Vendor.dto;

import java.util.Map;

public class VendorRequest {
    private String vendor;
    private Map<String, String> connectionParam;
    private String schemapath;

    // Getters and Setters
    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public Map<String, String> getConnectionParam() {
        return connectionParam;
    }

    public void setConnectionParam(Map<String, String> connectionParam) {
        this.connectionParam = connectionParam;
    }

    public String getSchemapath() {
        return schemapath;
    }

    public void setSchemapath(String schemapath) {
        this.schemapath = schemapath;
    }
}
