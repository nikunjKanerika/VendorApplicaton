package com.kanerika.Vendor.model;

public class PracticalAdvice {
    private String message;
    private int identifier;

    public PracticalAdvice() {
        // Default constructor needed for deserialization
    }

    public PracticalAdvice(String message, int identifier) {
        this.message = message;
        this.identifier = identifier;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return "PracticalAdvice{" +
                "message='" + message + '\'' +
                ", identifier=" + identifier +
                '}';
    }
}
