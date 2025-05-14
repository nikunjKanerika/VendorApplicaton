package com.kanerika.Vendor.dbClasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kanerika.Vendor.GeneralVendor;
import com.kanerika.Vendor.config.PostgresProperties;
import com.kanerika.Vendor.dto.VendorRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Postgres implements GeneralVendor {

    @Autowired
    PostgresProperties postgresProperties;

    @Override
    public String connect(VendorRequest request) {
        String username = postgresProperties.getUsername();
        String password = postgresProperties.getPassword();
        String schemaPath = request.getSchemapath();
        String offset = request.getOffset();
        String limit = request.getLimit();
        try {
            Class.forName(postgresProperties.getDriver());

            String url = "jdbc:postgresql://localhost:5432/test";

            Connection connection = DriverManager.getConnection(url, username, password);

            if (connection != null) {
                Statement stmt = connection.createStatement();

                // Build the query with pagination
                StringBuilder queryBuilder = new StringBuilder("SELECT * FROM " + schemaPath);

                // Add LIMIT if provided
                if (limit != null && !limit.isEmpty()) {
                    queryBuilder.append(" LIMIT ").append(limit);

                    // Add OFFSET only if LIMIT is provided (standard SQL practice)
                    if (offset != null && !offset.isEmpty()) {
                        queryBuilder.append(" OFFSET ").append(offset);
                    }
                }

                String query = queryBuilder.toString();
                ResultSet rs = stmt.executeQuery(query);
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                Map<String, Object> response = new HashMap<>();
                response.put("status", "success");
                response.put("message", "Connected to PostgreSQL successfully!");
                response.put("query", query); // Optional: include the executed query in response

                List<String> columns = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    columns.add(metaData.getColumnName(i));
                }
                response.put("columns", columns);

                // Get data rows
                List<Map<String, Object>> data = new ArrayList<>();
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        row.put(columnName, rs.getObject(i));
                    }
                    data.add(row);
                }
                response.put("data", data);

                rs.close();
                stmt.close();
                connection.close();

                return new com.google.gson.Gson().toJson(response);
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("status", "error");
                errorResponse.put("message", "Failed to connect to PostgreSQL.");
                return new com.google.gson.Gson().toJson(errorResponse);
            }
        } catch (ClassNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "PostgreSQL JDBC Driver not found");
            errorResponse.put("error", e.getMessage());
            return new com.google.gson.Gson().toJson(errorResponse);
        } catch (SQLException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Connection failed");
            errorResponse.put("error", e.getMessage());
            return new com.google.gson.Gson().toJson(errorResponse);
        }
    }
    public String connect(String host, String port,String username, String password) {
        String url = "jdbc:postgresql://" + host + ":" + port + "/" + postgresProperties.getDbName();


        try {
            Class.forName(postgresProperties.getDriver());

            Connection connection = DriverManager.getConnection(url, username, password);

            if (connection != null) {
                Statement stmt = connection.createStatement();

                String query = "SELECT * FROM employees";
                ResultSet rs = stmt.executeQuery(query);
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                Map<String, Object> response = new HashMap<>();
                response.put("status", "success");
                response.put("message", "Connected to PostgreSQL successfully!");

                List<String> columns = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    columns.add(metaData.getColumnName(i));
                }
                response.put("columns", columns);

                // Get data rows
                List<Map<String, Object>> data = new ArrayList<>();
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        row.put(columnName, rs.getObject(i));
                    }
                    data.add(row);
                }
                response.put("data", data);

                rs.close();
                stmt.close();
                connection.close();

                return new com.google.gson.Gson().toJson(response);
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("status", "error");
                errorResponse.put("message", "Failed to connect to PostgreSQL.");
                return new com.google.gson.Gson().toJson(errorResponse);
            }
        } catch (ClassNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "PostgreSQL JDBC Driver not found");
            errorResponse.put("error", e.getMessage());
            return new com.google.gson.Gson().toJson(errorResponse);
        } catch (SQLException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Connection failed");
            errorResponse.put("error", e.getMessage());
            return new com.google.gson.Gson().toJson(errorResponse);
        }
    }

    @Override
    public String connect(String s, String s1) {
        return "";
    }

    public String executeQuery(String query) {
        String url = "jdbc:postgresql://" + postgresProperties.getHost() + ":" + postgresProperties.getPort() + "/" + postgresProperties.getDbName();
        String username = postgresProperties.getUsername();
        String password = postgresProperties.getPassword();

        try {
            Class.forName(postgresProperties.getDriver());
            Connection connection = DriverManager.getConnection(url, username, password);

            if (connection != null) {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                Map<String, Object> response = new HashMap<>();
                response.put("status", "success");
                response.put("message", "Query executed successfully!");

                // Get column names
                List<String> columns = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    columns.add(metaData.getColumnName(i));
                }
                response.put("columns", columns);

                // Get data rows
                List<Map<String, Object>> data = new ArrayList<>();
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        row.put(columnName, rs.getObject(i));
                    }
                    data.add(row);
                }
                response.put("data", data);

                rs.close();
                stmt.close();
                connection.close();

                return new com.google.gson.Gson().toJson(response);
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("status", "error");
                errorResponse.put("message", "Failed to connect to PostgreSQL.");
                return new com.google.gson.Gson().toJson(errorResponse);
            }
        } catch (ClassNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "PostgreSQL JDBC Driver not found");
            errorResponse.put("error", e.getMessage());
            return new com.google.gson.Gson().toJson(errorResponse);
        } catch (SQLException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Query execution failed");
            errorResponse.put("error", e.getMessage());
            return new com.google.gson.Gson().toJson(errorResponse);
        }
    }



    public String executeUnloadData(String query, String format, String location) {
        String url = "jdbc:postgresql://" + postgresProperties.getHost() + ":" + postgresProperties.getPort() + "/" + postgresProperties.getDbName();
        String username = postgresProperties.getUsername();
        String password = postgresProperties.getPassword();

        // Default location if not provided
        if (location == null || location.isEmpty()) {
            location = "/home/kanerika/Desktop";
        }

        try {
            Class.forName(postgresProperties.getDriver());
            Connection connection = DriverManager.getConnection(url, username, password);

            if (connection != null) {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                Map<String, Object> response = new HashMap<>();

                if ("csv".equalsIgnoreCase(format)) {
                    // CSV Export Logic
                    String fileName = "export_" + System.currentTimeMillis() + ".csv";
                    String filePath = location.endsWith("/") ? location + fileName : location + "/" + fileName;

                    try (PrintWriter writer = new PrintWriter(new File(filePath))) {
                        // Write column headers
                        for (int i = 1; i <= columnCount; i++) {
                            writer.print(metaData.getColumnName(i));
                            if (i < columnCount) writer.print(",");
                        }
                        writer.println();

                        // Write data rows
                        while (rs.next()) {
                            for (int i = 1; i <= columnCount; i++) {
                                Object value = rs.getObject(i);
                                writer.print(value != null ? value.toString().replace("\"", "\"\"") : "");
                                if (i < columnCount) writer.print(",");
                            }
                            writer.println();
                        }

                        response.put("status", "success");
                        response.put("message", "Data exported to CSV successfully!");
                        response.put("file_path", filePath);
                    } catch (FileNotFoundException e) {
                        response.put("status", "error");
                        response.put("message", "Failed to create CSV file");
                        response.put("error", e.getMessage());
                    }
                } else if ("json".equalsIgnoreCase(format)) {
                    // JSON Export Logic
                    String fileName = "export_" + System.currentTimeMillis() + ".json";
                    String filePath = location.endsWith("/") ? location + fileName : location + "/" + fileName;

                    try (PrintWriter writer = new PrintWriter(new File(filePath))) {
                        // Prepare JSON structure
                        Map<String, Object> jsonResponse = new HashMap<>();
                        jsonResponse.put("status", "success");
                        jsonResponse.put("message", "Query executed successfully!");

                        // Get column names
                        List<String> columns = new ArrayList<>();
                        for (int i = 1; i <= columnCount; i++) {
                            columns.add(metaData.getColumnName(i));
                        }
                        jsonResponse.put("columns", columns);

                        // Get data rows
                        List<Map<String, Object>> data = new ArrayList<>();
                        while (rs.next()) {
                            Map<String, Object> row = new HashMap<>();
                            for (int i = 1; i <= columnCount; i++) {
                                String columnName = metaData.getColumnName(i);
                                row.put(columnName, rs.getObject(i));
                            }
                            data.add(row);
                        }
                        jsonResponse.put("data", data);

                        // Write JSON to file
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        String jsonOutput = gson.toJson(jsonResponse);
                        writer.write(jsonOutput);

                        response.put("status", "success");
                        response.put("message", "Data exported to JSON successfully!");
                        response.put("file_path", filePath);
                    } catch (FileNotFoundException e) {
                        response.put("status", "error");
                        response.put("message", "Failed to create JSON file");
                        response.put("error", e.getMessage());
                    }
                } else {
                    // Default behavior (return JSON response without saving to file)
                    response.put("status", "success");
                    response.put("message", "Query executed successfully!");

                    List<String> columns = new ArrayList<>();
                    for (int i = 1; i <= columnCount; i++) {
                        columns.add(metaData.getColumnName(i));
                    }
                    response.put("columns", columns);

                    List<Map<String, Object>> data = new ArrayList<>();
                    while (rs.next()) {
                        Map<String, Object> row = new HashMap<>();
                        for (int i = 1; i <= columnCount; i++) {
                            String columnName = metaData.getColumnName(i);
                            row.put(columnName, rs.getObject(i));
                        }
                        data.add(row);
                    }
                    response.put("data", data);
                }

                rs.close();
                stmt.close();
                connection.close();

                return new com.google.gson.Gson().toJson(response);
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("status", "error");
                errorResponse.put("message", "Failed to connect to PostgreSQL.");
                return new com.google.gson.Gson().toJson(errorResponse);
            }
        } catch (ClassNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "PostgreSQL JDBC Driver not found");
            errorResponse.put("error", e.getMessage());
            return new com.google.gson.Gson().toJson(errorResponse);
        } catch (SQLException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Query execution failed");
            errorResponse.put("error", e.getMessage());
            return new com.google.gson.Gson().toJson(errorResponse);
        }
    }
}
