package com.example.spring_boot.repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
import java.time.LocalDate;
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
        String sql = "SELECT policyNo, start_date, due_date, employee_id, car_id, plan_id FROM insurance WHERE car_id = ?";
        Insurance insurance = null;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, carId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                insurance = new Insurance();
                insurance.setPolicyNo(rs.getInt("policyNo"));
                insurance.setStartDate(rs.getDate("start_date"));
                insurance.setDueDate(rs.getDate("due_date"));
                insurance.setEmployeeId(rs.getLong("employee_id"));
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

    // Method to get all entries from the request_insurance table
    public static List<RequestInsurance> getAllRequestInsuranceEntries() {
        String sql = "SELECT r.req_id, r.plan_id, r.car_id, c.customer_id AS cust_id " +
                "FROM request_insurance r " +
                "JOIN cars c ON r.car_id = c.cid";
        List<RequestInsurance> requestInsuranceList = new ArrayList<>();

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                RequestInsurance requestInsurance = new RequestInsurance();
                requestInsurance.setReqId(rs.getInt("req_id"));
                requestInsurance.setPlanId(rs.getInt("plan_id"));
                requestInsurance.setCarId(rs.getLong("car_id"));
                requestInsurance.setCustId(rs.getLong("cust_id")); // Fetching and setting cust_id

                requestInsuranceList.add(requestInsurance);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return requestInsuranceList;
    }

    public static boolean addInsuranceEntry(Insurance insurance) {
        String insertInsuranceSql = "INSERT INTO insurance (start_date, due_date, employee_id, car_id, plan_id) VALUES (?, ?, ?, ?, ?)";
        String deleteRequestInsuranceSql = "DELETE FROM request_insurance WHERE car_id = ?";

        try (Connection conn = DatabaseConnector.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            // Insert into insurance table
            try (PreparedStatement insertStmt = conn.prepareStatement(insertInsuranceSql)) {
                insertStmt.setDate(1, new Date(insurance.getStartDate().getTime()));
                insertStmt.setDate(2, new Date(insurance.getDueDate().getTime()));
                insertStmt.setObject(3, insurance.getEmployeeId());
                insertStmt.setObject(4, insurance.getCarId());
                insertStmt.setInt(5, insurance.getPlanId());
                insertStmt.executeUpdate();
            }

            // Delete from request_insurance table
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteRequestInsuranceSql)) {
                deleteStmt.setLong(1, insurance.getCarId());
                deleteStmt.executeUpdate();
            }

            conn.commit(); // Commit transaction
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try (Connection conn = DatabaseConnector.getConnection()) {
                conn.rollback(); // Rollback transaction on error
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            return false;
        }
    }

    public static List<Insurance> getDueInsuranceByCustomerId(Long custId) {
        String fetchCarsSql = "SELECT cid FROM cars WHERE customer_id = ?";
        String fetchInsuranceSql = "SELECT * FROM insurance WHERE car_id = ? AND due_date <= ?";
        
        List<Insurance> dueInsuranceList = new ArrayList<>();

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement fetchCarsStmt = conn.prepareStatement(fetchCarsSql);
             PreparedStatement fetchInsuranceStmt = conn.prepareStatement(fetchInsuranceSql)) {

            // Set the customer ID for fetching car IDs
            fetchCarsStmt.setLong(1, custId);
            ResultSet carResultSet = fetchCarsStmt.executeQuery();

            // Set the current date
            Date currentDate = Date.valueOf(LocalDate.now());

            // Loop through each car of the customer
            while (carResultSet.next()) {
                long carId = carResultSet.getLong("cid");

                // Check insurance due date for each car
                fetchInsuranceStmt.setLong(1, carId);
                fetchInsuranceStmt.setDate(2, currentDate);
                
                ResultSet insuranceResultSet = fetchInsuranceStmt.executeQuery();

                // Collect insurance entries that meet the condition
                while (insuranceResultSet.next()) {
                    Insurance insurance = new Insurance();
                    insurance.setPolicyNo(insuranceResultSet.getInt("policyNo"));
                    insurance.setStartDate(insuranceResultSet.getDate("start_date"));
                    insurance.setDueDate(insuranceResultSet.getDate("due_date"));
                    insurance.setEmployeeId(insuranceResultSet.getLong("employee_id"));
                    insurance.setCarId(insuranceResultSet.getLong("car_id"));
                    insurance.setPlanId(insuranceResultSet.getInt("plan_id"));
                    
                    dueInsuranceList.add(insurance);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dueInsuranceList;
    }
}