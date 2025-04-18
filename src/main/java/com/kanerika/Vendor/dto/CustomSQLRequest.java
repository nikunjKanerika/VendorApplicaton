package com.kanerika.Vendor.dto;

public class CustomSQLRequest {
    private String connectionId;
    private String query;

    public CustomSQLRequest() {}

    public CustomSQLRequest(String connectionId, String query) {
        this.connectionId = connectionId;
        this.query = query;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return "CustomSQLRequest{" +
                "connectionId='" + connectionId + '\'' +
                ", query='" + query + '\'' +
                '}';
    }

}
