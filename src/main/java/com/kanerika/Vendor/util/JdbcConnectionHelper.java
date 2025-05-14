package com.kanerika.Vendor.util;

import com.kanerika.Vendor.aes.AesEncryptor;

import java.sql.*;

public class JdbcConnectionHelper {

    public static String getVendorByConnectionId(String connectionId) throws SQLException {
        String vendor = null;
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test", "postgres", "postgres")) {
            String query = "SELECT vendor FROM jdbc_connections WHERE connection_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, Integer.parseInt(connectionId));
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        vendor = rs.getString("vendor");
                    } else {
                        throw new IllegalArgumentException("No vendor found for connectionId: " + connectionId);
                    }
                }
            }
        }

        return vendor;
    }

    public static String[] getHostAndPortByConnectionId(String connectionId) throws SQLException {
        String[] hostAndPort = new String[2];

        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test", "postgres", "postgres")) {
            String query = "SELECT host, port FROM jdbc_connections WHERE connection_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, Integer.parseInt(connectionId));
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        hostAndPort[0] = rs.getString("host");
                        hostAndPort[1] = rs.getString("port");
                    } else {
                        throw new IllegalArgumentException("No connection found for connectionId: " + connectionId);
                    }
                }
            }
        }

        return hostAndPort;
    }

    public static String[] getUsernameAndPassword(String connectionId) throws SQLException {
        String[] usernamePassword = new String[2];


        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test", "postgres", "postgres")) {
            String query = "SELECT username, password FROM jdbc_connections WHERE connection_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setInt(1, Integer.parseInt(connectionId));
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        usernamePassword[0] = rs.getString("username");

                        usernamePassword[1] = rs.getString("password");
                        usernamePassword[0] = AesEncryptor.usernameDecryptor(usernamePassword[0]);
                        usernamePassword[1] = AesEncryptor.passwordDecryptor(usernamePassword[1]);

                    } else {
                        throw new IllegalArgumentException("No connection found for connectionId: " + connectionId);
                    }
                }
            }
        }

        return usernamePassword;
    }
}
