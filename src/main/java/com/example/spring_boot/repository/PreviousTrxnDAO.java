package com.example.spring_boot.repository;

import com.example.spring_boot.entity.*;
import com.example.spring_boot.config.*;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class PreviousTrxnDAO {

    public static Map<Insurance, Trxn> getInsuranceTransactionsByCustomerId(long customerId) {
        String fetchCarIdsSQL = "SELECT cid FROM cars WHERE customer_id = ?";
        String fetchInsuranceSQL = "SELECT plan_id, policyNo, start_date, due_date, employee_id FROM insurance WHERE car_id = ?";
        String fetchTransactionSQL = "SELECT t.trans_id, t.trans_desc, t.method, t.trans_date, t.total " +
                "FROM insurance_transaction it JOIN trxn t ON it.trans_id = t.trans_id " +
                "WHERE it.policyNo = ?";

        Map<Insurance, Trxn> insuranceTransactions = new HashMap<>();

        try (Connection conn = DatabaseConnector.getConnection()) {

            // Step 1: Fetch all car IDs associated with the customer_id
            try (PreparedStatement fetchCarIdsStmt = conn.prepareStatement(fetchCarIdsSQL)) {
                fetchCarIdsStmt.setLong(1, customerId);
                ResultSet carResultSet = fetchCarIdsStmt.executeQuery();

                while (carResultSet.next()) {
                    long carId = carResultSet.getLong("cid");

                    // Step 2: For each car_id, fetch the insurance details
                    try (PreparedStatement fetchInsuranceStmt = conn.prepareStatement(fetchInsuranceSQL)) {
                        fetchInsuranceStmt.setLong(1, carId);
                        ResultSet insuranceResultSet = fetchInsuranceStmt.executeQuery();

                        while (insuranceResultSet.next()) {
                            int policyNo = insuranceResultSet.getInt("policyNo");
                            Date startDate = insuranceResultSet.getDate("start_date");
                            Date endDate = insuranceResultSet.getDate("due_date");
                            long employeeId = insuranceResultSet.getLong("employee_id");
                            int planId = insuranceResultSet.getInt("plan_id");

                            // Create an Insurance object using setters
                            Insurance insurance = new Insurance();
                            insurance.setPolicyNo(policyNo);
                            insurance.setStartDate(startDate); // Keep as Date
                            insurance.setDueDate(endDate); // Keep as Date
                            insurance.setEmployeeId(employeeId);
                            insurance.setCarId(carId);
                            insurance.setPlanId(planId);

                            // Step 3: Fetch the corresponding transaction details using the policyNo
                            try (PreparedStatement fetchTrxnStmt = conn.prepareStatement(fetchTransactionSQL)) {
                                fetchTrxnStmt.setInt(1, policyNo);
                                ResultSet trxnResultSet = fetchTrxnStmt.executeQuery();

                                while (trxnResultSet.next()) {
                                    long transactionId = trxnResultSet.getLong("trans_id");
                                    String transDesc = trxnResultSet.getString("trans_desc");
                                    String method = trxnResultSet.getString("method");
                                    Timestamp transDate = trxnResultSet.getTimestamp("trans_date");
                                    int total = trxnResultSet.getInt("total");

                                    // Create a Trxn object using setters
                                    Trxn trxn = new Trxn();
                                    trxn.setTransId(transactionId);
                                    trxn.setTransDesc(transDesc);
                                    trxn.setMethod(method);
                                    trxn.setTransDate(new Date(transDate.getTime())); // Convert to Date
                                    trxn.setTotal(total);

                                    // Map the Insurance object to the Trxn object
                                    insuranceTransactions.put(insurance, trxn);
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return insuranceTransactions;
    }

    public static Map<Repair, Trxn> getRepairTransactionsByCustomerId(long customerId) {
        String fetchCarIdsSQL = "SELECT cid FROM cars WHERE customer_id = ?";
        String fetchRepairSQL = "SELECT employee_id,repair_id, car_id, transaction_id, repair_status FROM repair WHERE car_id = ? AND repair_status = 'PAYMENT RECEIVED'";
        String fetchTransactionSQL = "SELECT trans_id, trans_desc, method, trans_date, total FROM trxn WHERE trans_id = ?";

        Map<Repair, Trxn> repairTransactions = new HashMap<>();

        try (Connection conn = DatabaseConnector.getConnection()) {

            // Step 1: Fetch all car IDs associated with the customer_id
            try (PreparedStatement fetchCarIdsStmt = conn.prepareStatement(fetchCarIdsSQL)) {
                fetchCarIdsStmt.setLong(1, customerId);
                ResultSet carResultSet = fetchCarIdsStmt.executeQuery();

                while (carResultSet.next()) {
                    long carId = carResultSet.getLong("cid");

                    // Step 2: For each car_id, fetch the repair details with status "PAYMENT RECEIVED"
                    try (PreparedStatement fetchRepairStmt = conn.prepareStatement(fetchRepairSQL)) {
                        fetchRepairStmt.setLong(1, carId);
                        ResultSet repairResultSet = fetchRepairStmt.executeQuery();

                        while (repairResultSet.next()) {
                            long repairId = repairResultSet.getLong("repair_id");
                            long transactionId = repairResultSet.getLong("transaction_id");
                            String repairStatus = repairResultSet.getString("repair_status");
                            long id1= repairResultSet.getLong("employee_id");

                            // Create a Repair object
                            Repair repair = new Repair();
                            repair.setRepairId(repairId);
                            repair.setCarId(carId);
                            repair.setTransactionId(transactionId);
                            repair.setRepairStatus(repairStatus);
                            repair.setEmployeeId(id1);


                            // Step 3: Fetch the corresponding transaction details
                            try (PreparedStatement fetchTrxnStmt = conn.prepareStatement(fetchTransactionSQL)) {
                                fetchTrxnStmt.setLong(1, transactionId);
                                ResultSet trxnResultSet = fetchTrxnStmt.executeQuery();

                                if (trxnResultSet.next()) {
                                    long transId = trxnResultSet.getLong("trans_id");
                                    String transDesc = trxnResultSet.getString("trans_desc");
                                    String method = trxnResultSet.getString("method");
                                    Timestamp transDate = trxnResultSet.getTimestamp("trans_date");
                                    int total = trxnResultSet.getInt("total");

                                    // Create a Trxn object
                                    Trxn trxn = new Trxn();
                                    trxn.setTransId(transId);
                                    trxn.setTransDesc(transDesc);
                                    trxn.setMethod(method);
                                    trxn.setTransDate(new Date(transDate.getTime()));
                                    trxn.setTotal(total);

                                    // Map the Repair object to the Trxn object
                                    repairTransactions.put(repair, trxn);
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return repairTransactions;
    }


    public static Map<SellBuffer, Trxn> getSellBufferTransactionsByCustomerId(long customerId) {
        String fetchSellBufferSQL = "SELECT sell_req_id, model_id, transaction_id, empl_id, date_purchased, status FROM sell_buffer WHERE cust_id = ?";
        String fetchTransactionSQL = "SELECT trans_id, trans_desc, method, trans_date, total FROM trxn WHERE trans_id = ?";

        Map<SellBuffer, Trxn> sellBufferTransactions = new HashMap<>();

        try (Connection conn = DatabaseConnector.getConnection()) {

            // Step 1: Fetch all entries from sell_buffer associated with the customer_id
            try (PreparedStatement fetchSellBufferStmt = conn.prepareStatement(fetchSellBufferSQL)) {
                fetchSellBufferStmt.setLong(1, customerId);
                ResultSet sellBufferResultSet = fetchSellBufferStmt.executeQuery();

                while (sellBufferResultSet.next()) {
                    int sellReqId = sellBufferResultSet.getInt("sell_req_id");
                    long modelId = sellBufferResultSet.getLong("model_id");
                    Long transactionId = sellBufferResultSet.getLong("transaction_id"); // Can be null
                    Long emplId = sellBufferResultSet.getLong("empl_id"); // Can be null
                    Timestamp datePurchased = sellBufferResultSet.getTimestamp("date_purchased");
                    String status = sellBufferResultSet.getString("status");

                    // Create a SellBuffer object
                    SellBuffer sellBuffer = new SellBuffer();
                    sellBuffer.setSellReqId(sellReqId);
                    sellBuffer.setModelId(modelId);
                    sellBuffer.setCustId(customerId); // Store customerId directly in SellBuffer
                    sellBuffer.setTransactionId(transactionId);
                    sellBuffer.setEmplId(emplId);
                    sellBuffer.setDatePurchased(new Timestamp(datePurchased.getTime()));
                    sellBuffer.setStatus(status);

                    // Step 2: Fetch the corresponding transaction details if transactionId is not null
                    if (transactionId != null) {
                        try (PreparedStatement fetchTrxnStmt = conn.prepareStatement(fetchTransactionSQL)) {
                            fetchTrxnStmt.setLong(1, transactionId);
                            ResultSet trxnResultSet = fetchTrxnStmt.executeQuery();

                            if (trxnResultSet.next()) {
                                long transId = trxnResultSet.getLong("trans_id");
                                String transDesc = trxnResultSet.getString("trans_desc");
                                String method = trxnResultSet.getString("method");
                                Timestamp transDate = trxnResultSet.getTimestamp("trans_date");
                                int total = trxnResultSet.getInt("total");

                                // Create a Trxn object
                                Trxn trxn = new Trxn();
                                trxn.setTransId(transId);
                                trxn.setTransDesc(transDesc);
                                trxn.setMethod(method);
                                trxn.setTransDate(new Date(transDate.getTime()));
                            trxn.setTotal(total);

                                // Map the SellBuffer object to the Trxn object
                                sellBufferTransactions.put(sellBuffer, trxn);
                            }
                        }
                    } else {
                        // If there is no transactionId, just add the SellBuffer with a null Trxn
                        sellBufferTransactions.put(sellBuffer, null);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sellBufferTransactions;
    }
}
