package com.example.spring_boot.repository;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import com.example.spring_boot.config.*; 
public class PaymentDAO {

    public static boolean payInsurance(int policyNo, long employeeId) {
        String insertTrxnSql = "INSERT INTO trxn (trans_desc, method, trans_date, tot, employee_id) VALUES (?, ?, ?, ?, ?)";
        String insertRequestTrxnSql = "INSERT INTO insurance_transaction (policyNo, trans_id) VALUES (?, ?)";
        String updateDueDateSql = "UPDATE insurance SET due_date = ? WHERE policyNo = ?";
        String fetchPlanFrequencySql = "SELECT frequency FROM insurance_plan ip JOIN insurance i ON ip.plan_id = i.plan_id WHERE i.policyNo = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement insertTrxnStmt = conn.prepareStatement(insertTrxnSql, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement insertRequestTrxnStmt = conn.prepareStatement(insertRequestTrxnSql);
             PreparedStatement updateDueDateStmt = conn.prepareStatement(updateDueDateSql);
             PreparedStatement fetchPlanFrequencyStmt = conn.prepareStatement(fetchPlanFrequencySql)) {

            conn.setAutoCommit(false); // Start transaction

            // Step 1: Insert transaction in trxn
            insertTrxnStmt.setString(1, "Insurance Payment");
            insertTrxnStmt.setString(2, "ONLINE"); // Assuming payment method as ONLINE
            insertTrxnStmt.setDate(3, Date.valueOf(LocalDate.now())); // Set current date
            insertTrxnStmt.setString(4, "ONLINE");
            insertTrxnStmt.setLong(5, employeeId);
            int affectedRows = insertTrxnStmt.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                throw new SQLException("Failed to insert transaction.");
            }

            // Retrieve generated transaction ID
            int transactionId;
            try (ResultSet generatedKeys = insertTrxnStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    transactionId = generatedKeys.getInt(1);
                } else {
                    conn.rollback();
                    throw new SQLException("Failed to retrieve transaction ID.");
                }
            }

            // Step 2: Insert request transaction entry
            insertRequestTrxnStmt.setInt(1, policyNo);
            insertRequestTrxnStmt.setInt(2, transactionId);
            insertRequestTrxnStmt.executeUpdate();

            // Step 3: Determine next due date based on plan frequency
            fetchPlanFrequencyStmt.setInt(1, policyNo);
            ResultSet planResultSet = fetchPlanFrequencyStmt.executeQuery();
            if (!planResultSet.next()) {
                conn.rollback();
                throw new SQLException("Plan frequency not found for policy.");
            }

            String frequency = planResultSet.getString("frequency");
            LocalDate newDueDate = calculateNextDueDate(frequency);

            // Step 4: Update the due date in insurance table
            updateDueDateStmt.setDate(1, Date.valueOf(newDueDate));
            updateDueDateStmt.setInt(2, policyNo);
            updateDueDateStmt.executeUpdate();

            conn.commit(); // Commit transaction
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static LocalDate calculateNextDueDate(String frequency) {
        LocalDate currentDate = LocalDate.now();
        switch (frequency) {
            case "Monthly":
                return currentDate.plusMonths(1);
            case "Quarterly":
                return currentDate.plusMonths(3);
            case "Yearly":
                return currentDate.plusMonths(12);
            default:
                throw new IllegalArgumentException("Invalid frequency: " + frequency);
        }
    }
}
