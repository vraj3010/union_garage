package com.example.spring_boot.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.example.spring_boot.entity.*;
import com.example.spring_boot.config.*;
public class EmpPay{

    public static List<Insurance> getInsurancesByEmployeeIdWhereDueDatePassed(long employeeId) {
        String fetchInsuranceSQL = "SELECT policyNo, start_date, due_date, employee_id, car_id, plan_id " +
                                   "FROM insurance WHERE employee_id = ? AND due_date <= CURRENT_DATE";
        List<Insurance> insurances = new ArrayList<>();

        LocalDate currentDate = LocalDate.now(); // Get the current date

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(fetchInsuranceSQL)) {

            // Set the employee_id parameter
            stmt.setLong(1, employeeId);

            // Execute the query
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    Insurance insurance = new Insurance();
                    // Populate the Insurance object with data from the result set
                    insurance.setPolicyNo(resultSet.getInt("policyNo"));
                    insurance.setStartDate(resultSet.getDate("start_date")); // Directly set as java.sql.Date
                    insurance.setDueDate(resultSet.getDate("due_date")); // Directly set as java.sql.Date
                    insurance.setEmployeeId(resultSet.getLong("employee_id"));
                    insurance.setCarId(resultSet.getLong("car_id"));
                    insurance.setPlanId(resultSet.getInt("plan_id"));

                    // Add the Insurance object to the list
                    insurances.add(insurance);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // You can also print the current date if needed
        System.out.println("Current date: " + currentDate);

        return insurances;
    }

    public static List<SellBuffer> getPaymentDueSellBuffersByEmployeeId(long employeeId) {
        String fetchSellBufferSQL = "SELECT sell_req_id, model_id, cust_id, transaction_id, empl_id, date_purchased, status " +
                                     "FROM sell_buffer WHERE empl_id = ? AND status = 'PAYMENT DUE'";
        List<SellBuffer> sellBuffers = new ArrayList<>();

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(fetchSellBufferSQL)) {

            // Set the employee_id parameter
            stmt.setLong(1, employeeId);

            // Execute the query
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    SellBuffer sellBuffer = new SellBuffer();
                    // Populate the SellBuffer object with data from the result set
                    sellBuffer.setSellReqId(resultSet.getInt("sell_req_id"));
                    sellBuffer.setModelId(resultSet.getLong("model_id"));
                    sellBuffer.setCustId(resultSet.getLong("cust_id"));
                    sellBuffer.setTransactionId(resultSet.getLong("transaction_id"));
                    sellBuffer.setEmplId(resultSet.getLong("empl_id"));
                    sellBuffer.setDatePurchased(resultSet.getTimestamp("date_purchased"));
                    sellBuffer.setStatus(resultSet.getString("status"));

                    // Add the SellBuffer object to the list
                    sellBuffers.add(sellBuffer);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sellBuffers;
    }

    public static List<Repair> getRepairsByEmployeeIdWhereStatusPaymentDue(long employeeId) {
        String fetchRepairSQL = "SELECT repair_id, car_id, employee_id, repair_status, cost, transaction_id,repair_date " +
                                "FROM repair WHERE employee_id = ? AND repair_status = 'PAYMENT DUE'";
        List<Repair> repairs = new ArrayList<>();

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(fetchRepairSQL)) {

            // Set the employee_id parameter
            stmt.setLong(1, employeeId);

            // Execute the query
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    Repair repair = new Repair();
                    // Populate the Repair object with data from the result set
                    repair.setRepairId(resultSet.getLong("repair_id"));
                    repair.setRepairDate(resultSet.getDate("repair_date"));
                    repair.setCarId(resultSet.getLong("car_id"));
                    repair.setEmployeeId(resultSet.getLong("employee_id"));
                    repair.setRepairStatus(resultSet.getString("repair_status"));
                    repair.setCost(resultSet.getInt("cost"));
                    repair.setTransactionId(resultSet.getLong("transaction_id"));
                    

                    // Add the Repair object to the list
                    repairs.add(repair);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return repairs;
    }
}