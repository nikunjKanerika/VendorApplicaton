package com.kanerika.Vendor.dto;

public class ExtendedSQLRequestToUnloadData extends CustomSQLRequest{
    private String format;
    private String location;

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
