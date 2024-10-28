package com.example.spring_boot.repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.example.spring_boot.entity.*;
import com.example.spring_boot.config.*;
public class InsuranceDAO {

    public void addInsuranceEntry(Insurance insurance) {
        String sql = "INSERT INTO insurance (policyNo, start_date, end_date, premium_amt, coverage_amt, policy_status, deductible, employee_id, transaction_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, insurance.getPolicyNo());
            pstmt.setDate(2, new java.sql.Date(insurance.getStartDate().getTime()));
            pstmt.setDate(3, new java.sql.Date(insurance.getEndDate().getTime()));
            pstmt.setInt(4, insurance.getPremiumAmt());
            pstmt.setInt(5, insurance.getCoverageAmt());
            pstmt.setString(6, insurance.getPolicyStatus());
            pstmt.setInt(7, insurance.getDeductible());
            pstmt.setInt(8, insurance.getEmployeeId());
            pstmt.setInt(9, insurance.getTransactionId());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Insurance entry added successfully!");
            } else {
                System.out.println("Failed to add insurance entry.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
