package com.kanerika.Vendor.dto;

// Response DTO
public class ConnectionVendorsResponse {
    private String sourceVendor;
    private String destinationVendor;

    public ConnectionVendorsResponse(String sourceVendor, String destinationVendor) {
        this.sourceVendor = sourceVendor;
        this.destinationVendor = destinationVendor;
    }

    // Getters and setters
}
