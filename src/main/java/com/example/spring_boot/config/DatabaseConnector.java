package com.example.spring_boot.config;

import java.sql.*;

public class DatabaseConnector {
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/?user=root";
    private static final String USER = "root"; // replace with your MySQL username
    private static final String PASS = "NewPassword"; // replace with your MySQL password
    private static final String DB = "use mydb";

    // Method to establish connection
    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(DB);  // Replace 'ug' with your actual database name
        }
        return conn;
    }
}