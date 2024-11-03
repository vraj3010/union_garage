package com.example.spring_boot.repository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import java.sql.*;
import com.example.spring_boot.config.*;
import com.example.spring_boot.entity.Repair; 
public class PaymentDAO {

    public static boolean payInsurance(int policyNo, long employeeId, long custId) {
        String fetchPremiumAmountSql = "SELECT ip.premium_amt, ip.frequency " +
                                        "FROM insurance_plan ip JOIN insurance i ON ip.plan_id = i.plan_id " +
                                        "WHERE i.policyNo = ?";
        String insertTrxnSql = "INSERT INTO trxn (trans_desc, method, trans_date, total) VALUES (?, ?, ?, ?)";
        String insertRequestTrxnSql = "INSERT INTO insurance_transaction (policyNo, trans_id) VALUES (?, ?)";
        String updateDueDateSql = "UPDATE insurance SET due_date = ? WHERE policyNo = ?";
    
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement fetchPremiumAmountStmt = conn.prepareStatement(fetchPremiumAmountSql);
             PreparedStatement insertTrxnStmt = conn.prepareStatement(insertTrxnSql, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement insertRequestTrxnStmt = conn.prepareStatement(insertRequestTrxnSql);
             PreparedStatement updateDueDateStmt = conn.prepareStatement(updateDueDateSql)) {
    
            conn.setAutoCommit(false); // Start transaction
    
            // Step 1: Fetch premium amount and frequency
            fetchPremiumAmountStmt.setInt(1, policyNo);
            ResultSet premiumResult = fetchPremiumAmountStmt.executeQuery();
            if (!premiumResult.next()) {
                conn.rollback();
                throw new SQLException("Premium amount or frequency not found for policy.");
            }
    
            int premiumAmount = premiumResult.getInt("premium_amt");
            String frequency = premiumResult.getString("frequency");
    
            // Step 2: Insert transaction into trxn
            insertTrxnStmt.setString(1, "Insurance Payment");
            insertTrxnStmt.setString(2, "ONLINE"); // Assuming payment method as ONLINE
            insertTrxnStmt.setDate(3, Date.valueOf(LocalDate.now())); // Set current date
            insertTrxnStmt.setInt(4, premiumAmount); // Insert premium amount as total
    
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
    
            // Step 3: Insert request transaction entry
            insertRequestTrxnStmt.setInt(1, policyNo);
            insertRequestTrxnStmt.setInt(2, transactionId);
            insertRequestTrxnStmt.executeUpdate();
    
            // Step 4: Determine next due date based on plan frequency
            LocalDate newDueDate = calculateNextDueDate(frequency);
    
            // Step 5: Update the due date in insurance table
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
    
    // Utility function to calculate the next due date based on frequency
    private static LocalDate calculateNextDueDate(String frequency) {
        LocalDate currentDate = LocalDate.now();
        switch (frequency) {
            case "Monthly":
                return currentDate.plusMonths(1);
            case "Quarterly":
                return currentDate.plusMonths(3);
            case "Yearly":
                return currentDate.plusYears(1);
            default:
                throw new IllegalArgumentException("Unknown frequency: " + frequency);
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



    public static boolean processRepairPayment(Long repairId, Long custId, Long empId) {
        String fetchRepairCostSql = "SELECT cost FROM repair WHERE repair_id = ?";
        String insertTrxnSql = "INSERT INTO trxn (trans_desc, method, trans_date, total) VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
        String updateRepairSql = "UPDATE repair SET transaction_id = ?, repair_status = 'PAYMENT RECEIVED' WHERE repair_id = ?";
    
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement fetchCostStmt = conn.prepareStatement(fetchRepairCostSql);
             PreparedStatement trxnStmt = conn.prepareStatement(insertTrxnSql, Statement.RETURN_GENERATED_KEYS)) {
    
            // Step 1: Fetch the cost for the given repair ID
            fetchCostStmt.setLong(1, repairId);
            ResultSet costResult = fetchCostStmt.executeQuery();
            if (!costResult.next()) {
                throw new SQLException("Repair cost not found for the provided repair ID.");
            }
    
            int cost = costResult.getInt("cost");
    
            // Step 2: Insert a new transaction with the fetched cost as the total
            trxnStmt.setString(1, "Repair Payment");
            trxnStmt.setString(2, "ONLINE");
            trxnStmt.setInt(3, cost); // Set cost as total amount
            int affectedRows = trxnStmt.executeUpdate();
    
            if (affectedRows == 0) {
                throw new SQLException("Failed to create transaction, no rows affected.");
            }
    
            // Step 3: Retrieve the generated transaction ID
            Long transactionId;
            try (ResultSet generatedKeys = trxnStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    transactionId = generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Failed to create transaction, no ID obtained.");
                }
            }
    
            // Step 4: Update the repair entry with transaction ID and set status to "PAYMENT RECEIVED"
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

    

    public static void sellCar(int sellReqId, long custId, Long employeeId) {
        String fetchModelIdSQL = "SELECT model_id FROM sell_buffer WHERE sell_req_id = ?";
        String fetchPriceSQL = "SELECT price FROM catalogue WHERE model_id = ?";
        String insertTransactionSQL = "INSERT INTO trxn (trans_desc, method, total) VALUES ('SELL PAYMENT', 'ONLINE', ?)";
        String updateSellBufferSQL = "UPDATE sell_buffer SET transaction_id = ?, status = 'PAYMENT RECEIVED' WHERE sell_req_id = ?";
    
        Connection conn = null;
    
        try {
            conn = DatabaseConnector.getConnection();
            conn.setAutoCommit(false);
    
            // Step 1: Fetch model_id from sell_buffer
            PreparedStatement fetchModelStmt = conn.prepareStatement(fetchModelIdSQL);
            fetchModelStmt.setInt(1, sellReqId);
            ResultSet modelResult = fetchModelStmt.executeQuery();
            if (!modelResult.next()) {
                throw new SQLException("No matching model_id found for the provided sell_req_id.");
            }
            Long modelId = modelResult.getLong("model_id");
    
            // Step 2: Fetch price from catalogue based on model_id
            PreparedStatement fetchPriceStmt = conn.prepareStatement(fetchPriceSQL);
            fetchPriceStmt.setLong(1, modelId);
            ResultSet priceResult = fetchPriceStmt.executeQuery();
            if (!priceResult.next()) {
                throw new SQLException("No matching price found for the provided model_id.");
            }
            int price = priceResult.getInt("price");
    
            // Step 3: Insert a new transaction into the trxn table with the fetched price as total
            PreparedStatement pstmtInsert = conn.prepareStatement(insertTransactionSQL, Statement.RETURN_GENERATED_KEYS);
            pstmtInsert.setInt(1, price); // Set the price as total
            pstmtInsert.executeUpdate();
    
            // Retrieve the generated transaction ID
            ResultSet generatedKeys = pstmtInsert.getGeneratedKeys();
            long transactionId = -1;
            if (generatedKeys.next()) {
                transactionId = generatedKeys.getLong(1);
                System.out.println("Transaction added with ID: " + transactionId);
            } else {
                throw new SQLException("Failed to retrieve transaction ID.");
            }
    
            // Step 4: Update the sell_buffer with the new transaction ID
            PreparedStatement pstmtUpdate = conn.prepareStatement(updateSellBufferSQL);
            pstmtUpdate.setLong(1, transactionId);
            pstmtUpdate.setInt(2, sellReqId);
    
            int rowsAffected = pstmtUpdate.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Sell buffer updated with transaction ID and status set to PAYMENT RECEIVED.");
            } else {
                throw new SQLException("No matching sell buffer found for the provided sell_req_id.");
            }
    
            conn.commit();
    
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                    System.out.println("Transaction rolled back due to an error.");
                }
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException closeException) {
                    closeException.printStackTrace();
                }
            }
        }
    }
}
