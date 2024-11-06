package com.example.spring_boot.repository;
import com.example.spring_boot.config.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeEmailDAO {

    public static boolean addEmployeeEmails(long empId, List<String> emails) {
        String insertEmailSQL = "INSERT INTO emp_email (employee_id, email) VALUES (?, ?)";
        boolean isSuccess = true;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertEmailSQL)) {

            conn.setAutoCommit(false);

            for (String email : emails) {
                pstmt.setLong(1, empId);
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

    public static List<String> getEmailsByEmpId(long empId) {
        List<String> emails = new ArrayList<>();
        String fetchEmailsSQL = "SELECT email FROM  emp_email WHERE employee_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(fetchEmailsSQL)) {

            // Set the customer_id parameter
            pstmt.setLong(1, empId);

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

    public static void deleteEmailsByEmployeeId(long empId) {
       
        String fetchEmailsSQL = "delete FROM emp_email WHERE employee_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(fetchEmailsSQL)) {

            // Set the customer_id parameter
            pstmt.setLong(1, empId);

            // Execute the query and process the results
            int rowsAffected = pstmt.executeUpdate();;

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
