package com.example.spring_boot.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.example.spring_boot.entity.*;
import com.example.spring_boot.config.*;
public class TrxnDAO {

    public void addTrxnEntry(Trxn trxn) {
        String sql = "INSERT INTO trxn (trans_id, trans_desc, method, trans_date, tot, employee_id, customer_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, trxn.getTransId());
            pstmt.setString(2, trxn.getTransDesc());
            pstmt.setString(3, trxn.getMethod());
            pstmt.setDate(4, new java.sql.Date(trxn.getTransDate().getTime()));
            pstmt.setString(5, trxn.getTot());
            pstmt.setInt(6, trxn.getEmployeeId());
            pstmt.setInt(7, trxn.getCustomerId());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Transaction entry added successfully!");
            } else {
                System.out.println("Failed to add transaction entry.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
