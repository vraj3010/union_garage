package com.example.spring_boot.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.spring_boot.config.*;
import com.example.spring_boot.entity.SellBuffer;
public class SellCarDAO {
    public static boolean isSellQuantityAvailable(Long modelId) {
        String sql = "SELECT sell_quantity FROM inventory WHERE model_id = ?";
        boolean isAvailable = false;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, modelId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int sellQuantity = rs.getInt("sell_quantity");
                isAvailable = sellQuantity > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isAvailable;
    }

    // Method to add a new entry to the sell_buffer table
    public static boolean addBuyRequest(long modelId, long custId) {
        String sql = "INSERT INTO sell_buffer (model_id, cust_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, modelId);
            pstmt.setLong(2, custId);

            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0; // Returns true if the insertion was successful

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Returns false if there was an exception
        }
    }

    public static List<SellBuffer> getInProgressEntriesByCustId(long custId) {
        String sql = "SELECT * FROM sell_buffer WHERE cust_id = ? AND status = 'IN PROGRESS'";
        List<SellBuffer> sellBufferList = new ArrayList<>();

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, custId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                SellBuffer sellBuffer = new SellBuffer();
                sellBuffer.setSellReqId(rs.getInt("sell_req_id"));
                sellBuffer.setModelId(rs.getLong("model_id"));
                sellBuffer.setDatePurchased(rs.getTimestamp("date_purchased"));

                sellBufferList.add(sellBuffer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sellBufferList;
    }

    public static List<SellBuffer> getRejectedEntriesByCustId(long custId) {
        String sql = "SELECT * FROM sell_buffer WHERE cust_id = ? AND status = 'REQUEST REJECTED'";
        List<SellBuffer> sellBufferList = new ArrayList<>();

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, custId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                SellBuffer sellBuffer = new SellBuffer();
                sellBuffer.setSellReqId(rs.getInt("sell_req_id"));
                sellBuffer.setModelId(rs.getLong("model_id"));
                sellBuffer.setEmplId(rs.getLong("empl_id"));
                sellBuffer.setDatePurchased(rs.getTimestamp("date_purchased"));

                sellBufferList.add(sellBuffer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sellBufferList;
    }

    public static List<SellBuffer> getAllInProgressEntries() {
        List<SellBuffer> inProgressList = new ArrayList<>();
        String sql = "SELECT * FROM sell_buffer WHERE status = 'IN PROGRESS'";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                SellBuffer entry = new SellBuffer();
                entry.setSellReqId(rs.getInt("sell_req_id"));
                entry.setModelId(rs.getLong("model_id"));
                entry.setCustId(rs.getLong("cust_id"));
                entry.setDatePurchased(rs.getTimestamp("date_purchased"));

                inProgressList.add(entry);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inProgressList;
    }


    public static void updateSellBufferStatusToPaymentDue(int sellReqId, long modelId, long empId) {
        String updateSellBufferSql = "UPDATE sell_buffer SET status = 'PAYMENT DUE', empl_id = ? WHERE sell_req_id = ?";
        String updateInventorySql = "UPDATE inventory SET sell_quantity = sell_quantity - 1 WHERE model_id = ? AND sell_quantity > 0";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmtSellBuffer = conn.prepareStatement(updateSellBufferSql);
             PreparedStatement pstmtInventory = conn.prepareStatement(updateInventorySql)) {

            // Update sell_buffer status to PAYMENT DUE
            pstmtSellBuffer.setLong(1, empId); // Set the employee ID
            pstmtSellBuffer.setInt(2, sellReqId); // Set the sell_req_id
            int rowsAffectedSellBuffer = pstmtSellBuffer.executeUpdate();

            if (rowsAffectedSellBuffer > 0) {
                System.out.println("Sell buffer status updated to PAYMENT DUE successfully.");

                // Update inventory sell_quantity
                pstmtInventory.setLong(1, modelId); // Set the model_id
                int rowsAffectedInventory = pstmtInventory.executeUpdate();

                if (rowsAffectedInventory > 0) {
                    System.out.println("Sell quantity in inventory decremented successfully.");
                } else {
                    System.out.println("No available sell_quantity to decrement for model_id: " + modelId);
                }
            } else {
                System.out.println("No matching sell_req_id found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateSellBufferStatusToRejected(int sellReqId, Long empId) {
        String sql = "UPDATE sell_buffer SET status = 'REQUEST REJECTED', empl_id = ? WHERE sell_req_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, empId); // Set the employee ID
            pstmt.setInt(2, sellReqId); // Set the sell_req_id

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Sell buffer status updated to REQUEST REJECTED successfully.");
            } else {
                System.out.println("No matching sell_req_id found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<SellBuffer> getSellBufferEntriesByCustomerId(long custId) {
        String sql = "SELECT * FROM sell_buffer WHERE cust_id = ? AND status = 'PAYMENT DUE'";
        List<SellBuffer> sellBufferList = new ArrayList<>();

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, custId); // Set the customer ID
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                SellBuffer sellBuffer = new SellBuffer();
                sellBuffer.setSellReqId(rs.getInt("sell_req_id"));
                sellBuffer.setModelId(rs.getLong("model_id"));
                sellBuffer.setCustId(rs.getLong("cust_id"));
                sellBuffer.setEmplId(rs.getLong("empl_id"));

                sellBufferList.add(sellBuffer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sellBufferList;
    }

    public static boolean isPaymentDueForCustomer(long customerId) {
        String checkPaymentDueSQL = "SELECT 1 FROM sell_buffer WHERE cust_id = ? AND status = 'PAYMENT DUE' LIMIT 1";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(checkPaymentDueSQL)) {

            // Set the customer ID parameter
            stmt.setLong(1, customerId);

            // Execute the query
            try (ResultSet rs = stmt.executeQuery()) {
                // If a row is found, return true (indicating there is at least one entry with "PAYMENT DUE" status)
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}