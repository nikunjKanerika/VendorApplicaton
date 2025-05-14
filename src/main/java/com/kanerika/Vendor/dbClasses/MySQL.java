package com.kanerika.Vendor.dbClasses;

import com.google.gson.Gson;
import com.kanerika.Vendor.GeneralVendor;
import com.kanerika.Vendor.config.MySQLProperties;
import com.kanerika.Vendor.dto.VendorRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MySQL implements GeneralVendor {

    private static final Logger logger = LoggerFactory.getLogger(MySQL.class.getSimpleName());

    @Autowired
    MySQLProperties mySQLProperties;

    @Override
    public String connect(VendorRequest request) {


        String url = "jdbc:mysql://" + request.getConnectionParam().getHost() + ":" + request.getConnectionParam().getPort() + "/" + mySQLProperties.getDbName();
        String username = mySQLProperties.getUsername();
        String password = mySQLProperties.getPassword();

        try {
            Class.forName(mySQLProperties.getDriver());
            Connection connection = null;
            try {
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }

            if (connection != null) {
                Statement stmt = connection.createStatement();

                String query = "SELECT * FROM employees";
                ResultSet rs = stmt.executeQuery(query);
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                Map<String, Object> response = new HashMap<>();
                response.put("status", "success");
                response.put("message", "Connected to MySQL successfully!");

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

                rs.close();
                stmt.close();
                connection.close();

                return new Gson().toJson(response);
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("status", "error");
                errorResponse.put("message", "Failed to connect to MySQL.");
                return new Gson().toJson(errorResponse);
            }
        } catch (ClassNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "MySQL JDBC Driver not found");
            errorResponse.put("error", e.getMessage());
            return new Gson().toJson(errorResponse);
        } catch (SQLException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Connection failed");
            errorResponse.put("error", e.getMessage());
            return new Gson().toJson(errorResponse);
        }
    }

    @Override
    public String connect(String host, String port, String username, String password) {
        return "";
    }

    @Override
    public String connect(String s, String s1) {
        return "";
    }

    @Override
    public String executeQuery(String query) {
        return "";
    }

    @Override
    public String executeUnloadData(String query, String format, String location) {
        return "";
    }



}
