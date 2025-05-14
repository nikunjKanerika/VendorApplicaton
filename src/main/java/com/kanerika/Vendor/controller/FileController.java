package com.kanerika.Vendor.controller;

import com.kanerika.Vendor.aes.AesEncryptor;
import com.kanerika.Vendor.dto.SaveJdbcConnectionsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(
        origins = "*", // Replace with your frontend URL
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}
)
@Controller
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @PostMapping("/saveConnection")
    public ResponseEntity<String> saveFileConnection(@RequestBody SaveJdbcConnectionsRequest request) {

        String host = request.getConnectionParam().getHost();
        String port = request.getConnectionParam().getPort();
        String vendor = request.getVendor();
        String username = request.getConnectionParam().getUsername();
        String password = request.getConnectionParam().getPassword();
        String connectionName = request.getConnectionParam().getConnectionName();

        String dbName = "test";

        AesEncryptor.init();
        String encryptedUsername = AesEncryptor.usernameEncryptor(username);
        String encryptedPassword = AesEncryptor.passwordEncryptor(password);
        System.out.println("Encrypted Username: " + encryptedUsername);
        System.out.println("Encrypted password: " + encryptedPassword);



        String jdbcUrl = "jdbc:postgresql://" + "localhost" + ":" + "5432" + "/" + dbName;
        String resultMessage;

        try (Connection conn = DriverManager.getConnection(jdbcUrl, "postgres", "postgres")) {
            logger.info("Connected to PostgreSQL DB successfully.");
            String createTableSQL = "CREATE TABLE IF NOT EXISTS file_connections (" +
                    "connection_id SERIAL PRIMARY KEY, " +
                    "host VARCHAR(100), " +
                    "port VARCHAR(10), " +
                    "vendor VARCHAR(50), " +
                    "username VARCHAR(50), " +
                    "password VARCHAR(50), " +
                    "connection_name VARCHAR(50))";

            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(createTableSQL);
            }

            String insertSQL = "INSERT INTO file_connections (host, port, vendor, username,password,connection_name) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, host);
                pstmt.setString(2, port);
                pstmt.setString(3, vendor);
                pstmt.setString(4, encryptedUsername); // store encrypted value
                pstmt.setString(5, encryptedPassword); // store encrypted value
                pstmt.setString(6, connectionName);
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
}
