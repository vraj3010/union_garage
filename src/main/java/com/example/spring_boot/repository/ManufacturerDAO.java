package com.example.spring_boot.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.example.spring_boot.config.*;
import com.example.spring_boot.entity.*;
public class ManufacturerDAO {

    // Method to add a new manufacturer entry
    public static long addManufacturer(Manufacturer manufacturer) {
        String insertSQL = "INSERT INTO manufacturer (first_name, middle_name, last_name, location) VALUES (?, ?, ?, ?)";
        long manuId = -1;
    
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
    
            pstmt.setString(1, manufacturer.getFirstName());
            pstmt.setString(2, manufacturer.getMiddleName());
            pstmt.setString(3, manufacturer.getLastName());
            pstmt.setString(4, manufacturer.getLocation());
    
            int affectedRows = pstmt.executeUpdate();
    
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    manuId = generatedKeys.getLong(1); // Retrieve the generated manu_id
                    System.out.println("Inserted manufacturer with manu_id: " + manuId);
                }
            } else {
                throw new SQLException("Failed to insert manufacturer, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return manuId;
    }
    // Method to get all manufacturers
    public static List<Manufacturer> getAllManufacturers() {
        List<Manufacturer> manufacturerList = new ArrayList<>();
        String query = "SELECT * FROM manufacturer";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Manufacturer manufacturer = new Manufacturer();
                manufacturer.setManuId(rs.getLong("manu_id"));
                manufacturer.setFirstName(rs.getString("first_name"));
                manufacturer.setMiddleName(rs.getString("middle_name"));
                manufacturer.setLastName(rs.getString("last_name"));
                manufacturer.setLocation(rs.getString("location"));

                manufacturerList.add(manufacturer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return manufacturerList;
    }
    public static void addManufacturerEmails(long manuId, List<String> emails) {
        String insertEmailSQL = "INSERT INTO manu_email (manufacturer_id, email) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertEmailSQL)) {
    
            for (String email : emails) {
                pstmt.setLong(1, manuId);
                pstmt.setString(2, email);
                pstmt.addBatch();  // Batch insert for efficiency
            }
    
            pstmt.executeBatch();
            System.out.println("Emails added successfully for manufacturer ID: " + manuId);
    
        } catch (SQLException e) {
            e.printStackTrace();
    }
    }

    public static void addManufacturerMobileNumbers(long manuId, List<Long> mobileNumbers) {
        String insertMobileSQL = "INSERT INTO manu_mobile (manufacturer_id, mobileNo) VALUES (?, ?)";
    
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertMobileSQL)) {
    
            for (Long mobileNo : mobileNumbers) {
                pstmt.setLong(1, manuId);
                pstmt.setLong(2, mobileNo);
                pstmt.addBatch();  // Batch insert for efficiency
            }
    
            pstmt.executeBatch();
            System.out.println("Mobile numbers added successfully for manufacturer ID: " + manuId);
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getManufacturerEmails(long manuId) {
        List<String> emails = new ArrayList<>();
        String fetchEmailsSQL = "SELECT email FROM manu_email WHERE manufacturer_id = ?";
    
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(fetchEmailsSQL)) {
             
            pstmt.setLong(1, manuId);
            ResultSet rs = pstmt.executeQuery();
    
            while (rs.next()) {
                emails.add(rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return emails;
    }

    public static List<Long> getManufacturerMobileNumbers(long manuId) {
        List<Long> mobileNumbers = new ArrayList<>();
        String fetchMobilesSQL = "SELECT mobileNo FROM manu_mobile WHERE manufacturer_id = ?";
    
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(fetchMobilesSQL)) {
             
            pstmt.setLong(1, manuId);
            ResultSet rs = pstmt.executeQuery();
    
            while (rs.next()) {
                mobileNumbers.add(rs.getLong("mobileNo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return mobileNumbers;
    }

    public static Map<Long, String> getAllManufacturerDetails() {
        Map<Long, String> manufacturerDetails = new HashMap<>();
        String query = "SELECT manu_id, first_name, middle_name, last_name FROM manufacturer";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                long manufacturerId = rs.getLong("manu_id");
                String fullName = rs.getString("first_name") + " " +
                                  (rs.getString("middle_name") != null ? rs.getString("middle_name") + " " : "") +
                                  rs.getString("last_name");
                
                manufacturerDetails.put(manufacturerId, fullName.trim());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return manufacturerDetails;
    }

    public static boolean addCatalogueAndInventory(catalogue catalogue) {
        String catalogueQuery = "INSERT INTO catalogue (mileage, engine_type, price, model_name, manufacture_year, manufacturer_id) VALUES (?, ?, ?, ?, ?, ?)";
        String inventoryQuery = "INSERT INTO inventory (location, sell_quantity, rent_quantity, model_id) VALUES (?, ?, ?, ?)";
        
        boolean isAdded = false;

        try (Connection conn = DatabaseConnector.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            // Step 1: Add entry to the catalogue table
            try (PreparedStatement catalogueStmt = conn.prepareStatement(catalogueQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                catalogueStmt.setInt(1, catalogue.getMileage());
                catalogueStmt.setString(2, catalogue.getEngineType());
                catalogueStmt.setInt(3, catalogue.getPrice());
                catalogueStmt.setString(4, catalogue.getModelName());
                catalogueStmt.setInt(5, catalogue.getManufactureYear());
                catalogueStmt.setLong(6, catalogue.getManufacturerId());

                int catalogueRowsAffected = catalogueStmt.executeUpdate();

                if (catalogueRowsAffected > 0) {
                    // Retrieve the generated model_id
                    try (ResultSet generatedKeys = catalogueStmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            long modelId = generatedKeys.getLong(1);

                            // Step 2: Add corresponding entry to the inventory table
                            String location = "storage_" + (new Random().nextInt(10) + 1); // Generate random location
                            try (PreparedStatement inventoryStmt = conn.prepareStatement(inventoryQuery)) {
                                inventoryStmt.setString(1, location);
                                inventoryStmt.setInt(2, 0);  // sell_quantity = 0
                                inventoryStmt.setInt(3, 0);  // rent_quantity = 0
                                inventoryStmt.setLong(4, modelId);

                                int inventoryRowsAffected = inventoryStmt.executeUpdate();
                                isAdded = inventoryRowsAffected > 0;
                            }
                        }
                    }
                }
                
                conn.commit(); // Commit the transaction if both insertions succeed
            } catch (SQLException e) {
                conn.rollback(); // Rollback transaction if any error occurs
                e.printStackTrace();
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isAdded;
    }
}