package com.example.spring_boot.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.spring_boot.config.DatabaseConnector;
import com.example.spring_boot.entity.Manufacturer;

public class ManufacturerRepo {

    // Method to add a new manufacturer
    public static void addNewManufacturer(Long manuId) {
        String sql = "INSERT INTO manufacturer (manu_id) VALUES (?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setLong(1, manuId);
    
            pstmt.executeUpdate();
            System.out.println("Manufacturer with ID " + manuId + " added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    // Method to update manufacturer details based on attribute type (String)
    public static void updateManufacturerDetail(Long manuId, String column, String newValue) {
        String sql = "UPDATE manufacturer SET " + column + " = ? WHERE manu_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newValue);
            pstmt.setLong(2, manuId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Manufacturer details updated successfully.");
            } else {
                System.out.println("Manufacturer not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve a manufacturer by ID
    public static Manufacturer getManufacturerById(Long manuId) {
        String sql = "SELECT * FROM manufacturer WHERE manu_id = ?";
        Manufacturer manufacturerData = null;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, manuId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                manufacturerData = new Manufacturer();
                manufacturerData.setManuId(rs.getLong("manu_id"));
                manufacturerData.setFirstName(rs.getString("first_name"));
                manufacturerData.setMiddleName(rs.getString("middle_name"));
                manufacturerData.setLastName(rs.getString("last_name"));
                manufacturerData.setLocation(rs.getString("location"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return manufacturerData;
    }

    // Method to retrieve all manufacturers as a list
    public static List<Manufacturer> getAllManufacturers() {
        String sql = "SELECT * FROM manufacturer";
        List<Manufacturer> manufacturerList = new ArrayList<>();

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

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
}
