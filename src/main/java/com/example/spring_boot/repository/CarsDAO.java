package com.example.spring_boot.repository;
import com.example.spring_boot.config.*;
import com.example.spring_boot.entity.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CarsDAO {

    // Method to add a new car entry
    public static void addCarEntry(Cars car) {
        String sql = "INSERT INTO cars (vin, proof, customer_id) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

         
            pstmt.setString(1, car.getVin());
            pstmt.setBytes(2, car.getProof()); // Assuming proof is provided as byte[]
            pstmt.setInt(3, car.getCustomerId());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Car entry added successfully!");
            } else {
                System.out.println("Failed to add car entry.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
