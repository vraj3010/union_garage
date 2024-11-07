package com.example.spring_boot.repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.spring_boot.config.DatabaseConnector;
import com.example.spring_boot.entity.Rent;

public class RentCarDAO {
    public static void addNewRent(long renter_id, long model_id) {
        String sql = "INSERT INTO rent (start_date, end_date, model_id, renter_id, employee_id, rental_status) "
                   + "VALUES (NULL, NULL, ?, ?, NULL, 'Pending')";
        
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, model_id);  // Set model_id
            pstmt.setLong(2, renter_id); // Set renter_id

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("New rental record added successfully.");
            } else {
                System.out.println("Failed to add the rental record.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Rent> getPendingRentsByRenterId(long renter_id) {
        List<Rent> pendingRents = new ArrayList<>();
        String sql = "SELECT * FROM rent WHERE rental_status = 'Pending' AND renter_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, renter_id);  // Set renter_id parameter

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Rent rent = new Rent();
                    rent.setRent_id(rs.getLong("rent_id"));
                    rent.setStart_date(rs.getDate("start_date"));
                    rent.setEnd_date(rs.getDate("end_date"));
                    rent.setModel_id(rs.getLong("model_id"));
                    rent.setRenter_id(rs.getLong("renter_id"));
                    rent.setEmployee_id(rs.getLong("employee_id"));
                    rent.setRental_status(rs.getString("rental_status"));

                    pendingRents.add(rent);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pendingRents;
    }

    // Method to retrieve rejected rental records (status 'Cancelled') based on renter_id
    public static List<Rent> getRejectedRentsByRenterId(long renter_id) {
        List<Rent> rejectedRents = new ArrayList<>();
        String sql = "SELECT * FROM rent WHERE rental_status = 'Cancelled' AND renter_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, renter_id);  // Set renter_id parameter

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Rent rent = new Rent();
                    rent.setRent_id(rs.getLong("rent_id"));
                    rent.setStart_date(rs.getDate("start_date"));
                    rent.setEnd_date(rs.getDate("end_date"));
                    rent.setModel_id(rs.getLong("model_id"));
                    rent.setRenter_id(rs.getLong("renter_id"));
                    rent.setEmployee_id(rs.getLong("employee_id"));
                    rent.setRental_status(rs.getString("rental_status"));

                    rejectedRents.add(rent);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rejectedRents;
    }


    public static List<Rent> getPendingRents() {
        List<Rent> pendingRents = new ArrayList<>();
        String sql = "SELECT * FROM rent WHERE rental_status = 'Pending'";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Rent rent = new Rent();
                rent.setRent_id(rs.getLong("rent_id"));
                rent.setStart_date(rs.getDate("start_date"));
                rent.setEnd_date(rs.getDate("end_date"));
                rent.setModel_id(rs.getLong("model_id"));
                rent.setRenter_id(rs.getLong("renter_id"));
                rent.setEmployee_id(rs.getLong("employee_id"));
                rent.setRental_status(rs.getString("rental_status"));

                pendingRents.add(rent);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pendingRents;
    }

        // Update rental record with 'Active' status
        public static void updateRentToActive(long rent_id, long emp_id) {
            String sql = "UPDATE rent SET start_date = ?, end_date = ?, rental_status = 'Active', employee_id = ? "
                       + "WHERE rent_id = ?";
            
            // Set start_date to the current date
            java.util.Date startDate = new java.util.Date();
            
            // Calculate end_date as start_date + 2 years
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(java.util.Calendar.YEAR, 2);
            java.util.Date endDate = calendar.getTime();
    
            try (Connection conn = DatabaseConnector.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
                pstmt.setDate(1, new java.sql.Date(startDate.getTime()));  // Set start date
                pstmt.setDate(2, new java.sql.Date(endDate.getTime()));    // Set end date
                pstmt.setLong(3, emp_id);                                  // Set emp_id
                pstmt.setLong(4, rent_id);                                 // Identify the rent_id
    
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Rental record updated successfully to 'Active'.");
                } else {
                    System.out.println("Rental record not found or already assigned to an employee.");
                }
    
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    
        // Update rental record with 'Cancelled' status and set start_date and end_date to NULL
        public static void updateRentToCancelled(long rent_id, long emp_id) {
            String sql = "UPDATE rent SET start_date = NULL, end_date = NULL, rental_status = 'Cancelled', employee_id = ? "
                       + "WHERE rent_id = ?";
    
            try (Connection conn = DatabaseConnector.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
                pstmt.setLong(1, emp_id);  // Set emp_id
                pstmt.setLong(2, rent_id); // Identify the rent_id
    
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Rental record updated successfully to 'Cancelled'.");
                } else {
                    System.out.println("Rental record not found.");
                }
    
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }


        public static List<Rent> getActiveRentsByRenter(long renter_id) {
            List<Rent> activeRents = new ArrayList<>();
            String sql = "SELECT * FROM rent WHERE renter_id = ? AND rental_status = 'Active'";
    
            try (Connection conn = DatabaseConnector.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
                pstmt.setLong(1, renter_id); // Set renter_id parameter
    
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Rent rent = new Rent();
                        rent.setRent_id(rs.getLong("rent_id"));
                        rent.setStart_date(rs.getDate("start_date"));
                        rent.setEnd_date(rs.getDate("end_date"));
                        rent.setModel_id(rs.getLong("model_id"));
                        rent.setRenter_id(rs.getLong("renter_id"));
                        rent.setEmployee_id(rs.getLong("employee_id"));
                        rent.setRental_status(rs.getString("rental_status"));
    
                        activeRents.add(rent);
                    }
                }
    
            } catch (SQLException e) {
                e.printStackTrace();
            }
    
            return activeRents;
        }


        public static List<Rent> getRentsByEmployeeId(long emp_id) {
            List<Rent> rents = new ArrayList<>();
            String sql = "SELECT * FROM rent WHERE employee_id = ? AND rental_status='Active'";
    
            try (Connection conn = DatabaseConnector.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
                pstmt.setLong(1, emp_id);  // Set the employee ID parameter
    
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Rent rent = new Rent();
                        rent.setRent_id(rs.getLong("rent_id"));
                        rent.setStart_date(rs.getDate("start_date"));
                        rent.setEnd_date(rs.getDate("end_date"));
                        rent.setModel_id(rs.getLong("model_id"));
                        rent.setRenter_id(rs.getLong("renter_id"));
                        rent.setEmployee_id(rs.getLong("employee_id"));
                        rent.setRental_status(rs.getString("rental_status"));
    
                        rents.add(rent);
                    }
                }
    
            } catch (SQLException e) {
                e.printStackTrace();
            }
    
            return rents;
        }
}
