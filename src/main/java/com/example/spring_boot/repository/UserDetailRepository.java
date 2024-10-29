package com.example.spring_boot.repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.spring_boot.entity.UserDetails;
import com.example.spring_boot.config.DatabaseConnector;

public class UserDetailRepository {
    // Method to add a new customer
    public static void add_new_cust(Long cust_id) {
        String sql = "INSERT INTO customers (cust_id) VALUES (?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, cust_id);
            pstmt.executeUpdate();
            System.out.println("Customer added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update customer details
    public static void upd_cust_detail(Long cust_id, String column, String newValue) {
        String sql = "UPDATE customers SET " + column + " = ? WHERE cust_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newValue);
            pstmt.setLong(2, cust_id);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Customer details updated successfully.");
            } else {
                System.out.println("Customer not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Overloaded method to update numeric fields like phone number, AadharNo
    public static void upd_cust_detail(Long cust_id, String column, BigDecimal newValue) {
        String sql = "UPDATE customers SET " + column + " = ? WHERE cust_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBigDecimal(1, newValue);
            pstmt.setLong(2, cust_id);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Customer details updated successfully.");
            } else {
                System.out.println("Customer not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to query a customer by ID and return as a UserDetails object
    public static UserDetails getCustomerById(Long custId) {
        String sql = "SELECT * FROM customers WHERE cust_id = ?";
        UserDetails customerData = null; // Initialize as null

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, custId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                customerData = new UserDetails(); // Initialize customerData here
                int columnCount = rs.getMetaData().getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = rs.getMetaData().getColumnName(i);
                    Object columnValue = rs.getObject(i);

                    // Fixing variable name from userDetails to customerData
                    switch (columnName) {
                        case "cust_id": // Changed to match database column name
                            customerData.setCustId((Long) columnValue);
                            break;
                        case "phoneNo":
                            customerData.setPhoneNo((BigDecimal) columnValue);
                            break;
                        case "firstName":
                            customerData.setFirstName((String) columnValue);
                            break;
                        case "middleName":
                            customerData.setMiddleName((String) columnValue);
                            break;
                        case "lastName":
                            customerData.setLastName((String) columnValue);
                            break;
                        case "houseNo":
                            customerData.setHouseNo((String) columnValue);
                            break;
                        case "street":
                            customerData.setStreet((String) columnValue);
                            break;
                        case "city":
                            customerData.setCity((String) columnValue);
                            break;
                        case "state":
                            customerData.setState((String) columnValue);
                            break;
                        case "AadharNo":
                            customerData.setAadharNo((BigDecimal) columnValue);
                            break;
                        case "license":
                            customerData.setLicense((String) columnValue);
                            break;
                        default:
                            break; // or handle unexpected columns
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerData; // This could return null if not found
    }

    public static void deleteCustomerById(Long custId) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, custId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Customer deleted successfully!");
            } else {
                System.out.println("No customer found for the given cust_id.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
