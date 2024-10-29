package com.example.spring_boot.repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.example.spring_boot.config.*;
import com.example.spring_boot.entity.*;
public class RequestInsuranceDAO {

    public static boolean addRequestInsurance(int planId, long carId) {
        String sql = "INSERT INTO request_insurance (plan_id, car_id) VALUES (?, ?)";
        boolean isInserted = false;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, planId);
            pstmt.setLong(2, carId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                isInserted = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isInserted;
    }

    public static boolean isCarInsured(long carId) {
        String sql = "SELECT 1 FROM insurance WHERE car_id = ?";
        boolean exists = false;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, carId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                exists = true; // If a row is returned, the car is insured
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exists;
    }

    public static Insurance getInsuranceByCarId(long carId) {
        String sql = "SELECT policyNo, start_date, end_date, employee_id, transaction_id, car_id, plan_id FROM insurance WHERE car_id = ?";
        Insurance insurance = null;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, carId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                insurance = new Insurance();
                insurance.setPolicyNo(rs.getInt("policyNo"));
                insurance.setStartDate(rs.getDate("start_date"));
                insurance.setEndDate(rs.getDate("end_date"));
                insurance.setEmployeeId(rs.getInt("employee_id"));
                insurance.setTransactionId(rs.getLong("transaction_id"));
                insurance.setCarId(rs.getLong("car_id"));
                insurance.setPlanId(rs.getInt("plan_id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return insurance;
    }

    public static boolean doesCarExistInRequestInsurance(long carId) {
        String sql = "SELECT COUNT(*) FROM request_insurance WHERE car_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, carId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0; // Returns true if count is greater than 0
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if there was an error or no rows found
    }

    public static int getPlanIdFromRequestInsurance(Long carId) {
        String sql = "SELECT plan_id FROM request_insurance WHERE car_id = ?";
        int planId = 0; // Default value if no result is found
    
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setLong(1, carId);
            ResultSet rs = pstmt.executeQuery();
    
            if (rs.next()) { // Check if there is a result
                planId = rs.getInt("plan_id");
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return planId; // Return the plan ID, or 0 if no rows were found
    }
    

    // Method to delete an entry in the request_insurance table by car_id
    public static boolean deleteEntryByCarId(Long carId) {
        String sql = "DELETE FROM request_insurance WHERE car_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, carId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Return true if a row was deleted
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if there was an error or no rows deleted
    }
}