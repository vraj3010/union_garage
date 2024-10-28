package com.example.spring_boot.repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.example.spring_boot.entity.*;
import com.example.spring_boot.config.*;
public class RepairDAO {

    public void addRepairEntry(Repair repair) {
        String sql = "INSERT INTO repair (repair_id, car_id, repair_desc, repair_status, repair_date, cost, employee_id, customer_id, transaction_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, repair.getRepairId());
            pstmt.setInt(2, repair.getCarId());
            pstmt.setString(3, repair.getRepairDesc());
            pstmt.setString(4, repair.getRepairStatus());
            pstmt.setDate(5, new java.sql.Date(repair.getRepairDate().getTime()));
            pstmt.setInt(6, repair.getCost());
            pstmt.setInt(7, repair.getEmployeeId());
            pstmt.setInt(8, repair.getCustomerId());
            pstmt.setInt(9, repair.getTransactionId());

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
}
