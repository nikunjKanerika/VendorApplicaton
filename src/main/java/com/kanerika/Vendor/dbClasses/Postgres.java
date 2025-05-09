package com.kanerika.Vendor.dbClasses;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String url = "jdbc:postgresql://" + request.getConnectionParam().getHost() + ":" + request.getConnectionParam().getPort() + "/" + postgresProperties.getDbName();
        String username = postgresProperties.getUsername();
        String password = postgresProperties.getPassword();

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
}
