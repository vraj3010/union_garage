package com.example.spring_boot.repository;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import java.sql.*;
import com.example.spring_boot.config.*;
import com.example.spring_boot.entity.Repair; 
public class PaymentDAO {

    public static boolean payInsurance(int policyNo, long employeeId,long custid) {
        String insertTrxnSql = "INSERT INTO trxn (trans_desc, method, trans_date, tot, employee_id,customer_id) VALUES (?, ?, ?, ?, ?,?)";
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
            insertTrxnStmt.setLong(6, custid);

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


    public static List<Repair> getRepairsWithPaymentDueByCustomerId(Long customerId) {
        List<Repair> repairList = new ArrayList<>();

        // SQL to get all car IDs owned by the customer
        String carSql = "SELECT cid FROM cars WHERE customer_id = ?";

        // SQL to get repairs with status "PAYMENT DUE" for each car
        String repairSql = "SELECT repair_id, car_id, repair_status, repair_date, cost, employee_id " +
                "FROM repair WHERE car_id = ? AND repair_status = 'PAYMENT DUE'";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement carStmt = conn.prepareStatement(carSql)) {

            carStmt.setLong(1, customerId);
            ResultSet carRs = carStmt.executeQuery();

            while (carRs.next()) {
                int carId = carRs.getInt("cid");

                try (PreparedStatement repairStmt = conn.prepareStatement(repairSql)) {
                    repairStmt.setInt(1, carId);
                    ResultSet repairRs = repairStmt.executeQuery();

                    while (repairRs.next()) {
                        Repair repair = new Repair();
                        repair.setRepairId(repairRs.getLong("repair_id"));
                        repair.setCarId(repairRs.getLong("car_id"));
                        repair.setRepairStatus(repairRs.getString("repair_status"));
                        repair.setRepairDate(repairRs.getDate("repair_date"));
                        repair.setCost(repairRs.getInt("cost"));
                        repair.setEmployeeId(repairRs.getLong("employee_id"));

                        repairList.add(repair);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return repairList;
    }



    public static boolean processRepairPayment(Long repairId,Long custid,Long empId) {
        String insertTrxnSql = "INSERT INTO trxn (trans_desc, method, trans_date, tot, employee_id, customer_id) VALUES (?, ?, CURRENT_TIMESTAMP, 'OFFLINE', ?, ?)";
        String updateRepairSql = "UPDATE repair SET transaction_id = ?, repair_status = 'PAYMENT RECEIVED' WHERE repair_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement trxnStmt = conn.prepareStatement(insertTrxnSql, Statement.RETURN_GENERATED_KEYS)) {

            // Step 1: Insert new transaction
            trxnStmt.setString(1, "Repair Payment");
            trxnStmt.setString(2, "ONLINE");
            trxnStmt.setLong(3, empId);
            trxnStmt.setLong(4, custid);
            int affectedRows = trxnStmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Failed to create transaction, no rows affected.");
            }

            // Step 2: Retrieve the generated transaction ID
            Long transactionId;
            try (ResultSet generatedKeys = trxnStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    transactionId = generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Failed to create transaction, no ID obtained.");
                }
            }

            // Step 3: Update the repair entry with transaction ID and set status to "PAYMENT RECEIVED"
            try (PreparedStatement repairStmt = conn.prepareStatement(updateRepairSql)) {
                repairStmt.setLong(1, transactionId);
                repairStmt.setLong(2, repairId);
                repairStmt.executeUpdate();
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
