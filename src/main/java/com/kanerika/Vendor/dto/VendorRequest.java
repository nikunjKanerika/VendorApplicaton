package com.kanerika.Vendor.dto;

public class VendorRequest {
    private String vendor;
    private ConnectionParam connectionParam;
    private String schemapath;
    private String connectionId;
    private String table;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    // Getters and Setters
    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public ConnectionParam getConnectionParam() {
        return connectionParam;
    }

    public void setConnectionParam(ConnectionParam connectionParam) {
        this.connectionParam = connectionParam;
    }

    public String getSchemapath() {
        return schemapath;
    }

    public void setSchemapath(String schemapath) {
        this.schemapath = schemapath;
    }
}
