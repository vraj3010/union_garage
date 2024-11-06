package com.example.spring_boot.repository;
import com.example.spring_boot.config.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerEmailDAO {

    public static boolean addCustomerEmails(long customerId, List<String> emails) {
        String insertEmailSQL = "INSERT INTO cust_email (customer_id, email) VALUES (?, ?)";
        boolean isSuccess = true;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertEmailSQL)) {

            conn.setAutoCommit(false);

            for (String email : emails) {
                pstmt.setLong(1, customerId);
                pstmt.setString(2, email);
                pstmt.addBatch();
            }

            // Execute the batch
            pstmt.executeBatch();

            // Commit the transaction
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isSuccess;
    }

    public static List<String> getEmailsByCustomerId(long customerId) {
        List<String> emails = new ArrayList<>();
        String fetchEmailsSQL = "SELECT email FROM cust_email WHERE customer_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(fetchEmailsSQL)) {

            // Set the customer_id parameter
            pstmt.setLong(1, customerId);

            // Execute the query and process the results
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String email = rs.getString("email");
                emails.add(email);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return emails;
    }

    public static void deleteEmailsByCustomerId(long customerId) {
       
        String fetchEmailsSQL = "delete FROM cust_email WHERE customer_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(fetchEmailsSQL)) {

            // Set the customer_id parameter
            pstmt.setLong(1, customerId);

            // Execute the query and process the results
            int rowsAffected = pstmt.executeUpdate();;

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
