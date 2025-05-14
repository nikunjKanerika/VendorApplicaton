package com.kanerika.Vendor.dto;

public class SaveJdbcConnectionsRequest {
    private String vendor;
    private ConnectionParam connectionParam;

    public SaveJdbcConnectionsRequest(String vendor, ConnectionParam connectionParam) {
        this.vendor = vendor;
        this.connectionParam = connectionParam;
    }

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
}
