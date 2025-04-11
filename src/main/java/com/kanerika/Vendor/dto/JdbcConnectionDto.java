package com.kanerika.Vendor.dto;

public class JdbcConnectionDto {
    private int connectionId;
    private String host;
    private String port;
    private String vendor;
    private String username;  // Will store decrypted value if needed
    private String password;  // Will store decrypted value if needed

    // Constructor without username/password (for basic listing)
    public JdbcConnectionDto(int connectionId, String host, String port, String vendor) {
        this.connectionId = connectionId;
        this.host = host;
        this.port = port;
        this.vendor = vendor;
    }

    // Full constructor (include if you need to expose credentials)
    public JdbcConnectionDto(int connectionId, String host, String port,
                             String vendor, String username, String password) {
        this(connectionId, host, port, vendor);
        this.username = username;
        this.password = password;
    }

    public JdbcConnectionDto(int connectionId, String host, String port, String vendor, String username, String password, Object o) {
    }

    // Getters and Setters
    public int getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}