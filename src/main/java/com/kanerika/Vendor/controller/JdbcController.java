package com.kanerika.Vendor.controller;
import com.kanerika.Vendor.aes.AesEncryptor;
import com.kanerika.Vendor.dto.JdbcConnectionDto;
import com.kanerika.Vendor.dto.SaveJdbcConnectionsRequest;
import com.kanerika.Vendor.dto.UpdateConnectionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/savejdbcConnections")
@CrossOrigin(
        origins = "*", // Replace with your frontend URL
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}
)
public class JdbcController {


    private static final Logger logger = LoggerFactory.getLogger(JdbcController.class);

    @PostMapping
    public ResponseEntity<String> connectToJdbc(@RequestBody SaveJdbcConnectionsRequest request) {

        String host = request.getConnectionParam().getHost();
        String port = request.getConnectionParam().getPort();
        String vendor = request.getVendor();
        String username = request.getConnectionParam().getUsername();
        String password = request.getConnectionParam().getPassword();

        String dbName = "test";

        AesEncryptor.init();
        String encryptedUsername = AesEncryptor.usernameEncryptor(username);
        String encryptedPassword = AesEncryptor.passwordEncryptor(password);
        System.out.println("Encrypted Username: " + encryptedUsername);
        System.out.println("Encrypted password: " + encryptedPassword);



        String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + "/" + dbName;
        String resultMessage;

        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password)) {
            logger.info("Connected to PostgreSQL DB successfully.");
            String createTableSQL = "CREATE TABLE IF NOT EXISTS jdbc_connections (" +
                    "connection_id SERIAL PRIMARY KEY, " +
                    "host VARCHAR(100), " +
                    "port VARCHAR(10), " +
                    "vendor VARCHAR(50), " +
                    "username VARCHAR(50), " +
                    "password VARCHAR(50))";

            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(createTableSQL);
            }

            String insertSQL = "INSERT INTO jdbc_connections (host, port, vendor, username, password) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, host);
                pstmt.setString(2, port);
                pstmt.setString(3, vendor);
                pstmt.setString(4, encryptedUsername); // store encrypted value
                pstmt.setString(5, encryptedPassword); // store encrypted value
                pstmt.executeUpdate();

                // Fetch the generated connection_id
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int connectionId = generatedKeys.getInt(1);
                        resultMessage = "{\"connection_id\": " + connectionId + "}";
                    } else {
                        resultMessage = "{\"message\":\"Connection saved successfully, but no connection_id returned.\"}";
                    }
                }
            }

        } catch (SQLException e) {
            logger.error("Database error: ", e);
            resultMessage = "{\"error\":\"" + e.getMessage() + "\"}";
            return ResponseEntity.status(500).body(resultMessage);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return ResponseEntity.ok()
                .headers(headers)
                .body(resultMessage);
    }

    @PutMapping("/edit/{connectionId}")
    public ResponseEntity<String> updateConnection(
            @PathVariable int connectionId,
            @RequestBody UpdateConnectionRequest request) {

        // Encrypt the new credentials
        AesEncryptor.init();
        String encryptedUsername = AesEncryptor.usernameEncryptor(request.getUsername());
        String encryptedPassword = AesEncryptor.passwordEncryptor(request.getPassword());

        System.out.println("Encrypted Updated Username: " + encryptedUsername);
        System.out.println("Encrypted Updated Password: " + encryptedPassword);

        String url = "jdbc:postgresql://localhost:5432/test"; // Your local DB URL
        String adminUser = "postgres"; // Your DB admin user
        String adminPass = "postgres"; // Your DB admin password
        String resultMessage;

        try (Connection conn = DriverManager.getConnection(url, adminUser, adminPass)) {
            String updateSQL = "UPDATE jdbc_connections SET " +
                    "username = ?, " +
                    "password = ? " +
                    "WHERE connection_id = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
                pstmt.setString(1, encryptedUsername);
                pstmt.setString(2, encryptedPassword);
                pstmt.setInt(3, connectionId);

                int rowsUpdated = pstmt.executeUpdate();

                if (rowsUpdated > 0) {
                    resultMessage = "{\"status\":\"success\", \"message\":\"Credentials updated successfully\", \"connection_id\": " + connectionId + "}";
                } else {
                    resultMessage = "{\"status\":\"error\", \"message\":\"No connection found with ID: " + connectionId + "\"}";
                    return ResponseEntity.status(404).body(resultMessage);
                }
            }
        } catch (SQLException e) {
            logger.error("Database error while updating credentials: ", e);
            resultMessage = "{\"status\":\"error\", \"message\":\"" + e.getMessage() + "\"}";
            return ResponseEntity.status(500).body(resultMessage);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return ResponseEntity.ok()
                .headers(headers)
                .body(resultMessage);
    }

    @GetMapping("/readConnection")
    public ResponseEntity<Map<String, Object>> readConnections() {
        Map<String, Object> response = new HashMap<>();
        List<JdbcConnectionDto> connections = new ArrayList<>();
        String url = "jdbc:postgresql://localhost:5432/test";

        try (Connection conn = DriverManager.getConnection(url, "postgres", "postgres")) {
            String query = "SELECT connection_id, host, port, vendor, username, password FROM jdbc_connections";

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                AesEncryptor.init(); // Initialize decryption

                while (rs.next()) {
                    // Get encrypted values from database
                    String encryptedUsername = rs.getString("username");
                    String encryptedPassword = rs.getString("password");

                    // Decrypt the values
                    String decryptedUsername = AesEncryptor.usernameDecryptor(encryptedUsername);
                    String decryptedPassword = AesEncryptor.passwordDecryptor(encryptedPassword);

                    // Create DTO with decrypted values
                    JdbcConnectionDto dto = new JdbcConnectionDto(
                            rs.getInt("connection_id"),
                            rs.getString("host"),
                            rs.getString("port"),
                            rs.getString("vendor"),
                            decryptedUsername,  // Use decrypted username
                            decryptedPassword   // Use decrypted password
                    );

                    connections.add(dto);
                }
            }

            response.put("status", "success");
            response.put("data", connections);
            return ResponseEntity.ok(response);

        } catch (SQLException e) {
            response.put("status", "error");
            response.put("message", "Database error: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    @DeleteMapping("/delete/{connectionId}")
    public ResponseEntity<Map<String, String>> deleteConnection(
            @PathVariable int connectionId) {

        Map<String, String> response = new HashMap<>();
        String url = "jdbc:postgresql://localhost:5432/test";

        try (Connection conn = DriverManager.getConnection(url, "postgres", "postgres")) {
            String deleteSQL = "DELETE FROM jdbc_connections WHERE connection_id = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
                pstmt.setInt(1, connectionId);

                int rowsDeleted = pstmt.executeUpdate();

                if (rowsDeleted > 0) {
                    response.put("status", "success");
                    response.put("message", "Connection with ID " + connectionId + " deleted successfully");
                    return ResponseEntity.ok(response);
                } else {
                    response.put("status", "error");
                    response.put("message", "No connection found with ID: " + connectionId);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }
            }
        } catch (SQLException e) {
            logger.error("Database error while deleting connection: ", e);
            response.put("status", "error");
            response.put("message", "Failed to delete connection: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}


