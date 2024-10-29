package com.example.spring_boot.repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.spring_boot.entity.*;
import com.example.spring_boot.config.*;
public class RepairDAO {

    public static void addRepairEntry(Repair repair) {
        String sql = "INSERT INTO repair (car_id, repair_desc,customer_id) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, repair.getCarId());
            pstmt.setString(2, repair.getRepairDesc());
            pstmt.setLong(3, repair.getCustomerId());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Repair entry added successfully!");
            } else {
                System.out.println("Failed to add repair entry.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static boolean carExistsWithDefaultRepairStatus(Long cid) {
        String sql = "SELECT 1 FROM repair WHERE car_id = ? AND repair_status = 'IN PROGRESS'";
        boolean carExists = false;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, cid);
            ResultSet rs = pstmt.executeQuery();

            carExists = rs.next(); // Returns true if a matching record is found

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return carExists;
    }
}
