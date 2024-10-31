package com.example.spring_boot.repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
import com.example.spring_boot.entity.*;
import com.example.spring_boot.config.*;
public class RepairDAO {

    public static void addRepairEntry(Repair repair) {
        String sql = "INSERT INTO repair (car_id, repair_desc) VALUES (?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, repair.getCarId());
            pstmt.setString(2, repair.getRepairDesc());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Repair entry added successfully!");
            } else {
                System.out.println("Failed to add repair entry.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static boolean carExistsWithDefaultRepairStatus(Long cid) {
        String sql = "SELECT 1 FROM repair WHERE car_id = ? AND repair_status = 'IN PROGRESS'";
        boolean carExists = false;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, cid);
            ResultSet rs = pstmt.executeQuery();

            carExists = rs.next(); // Returns true if a matching record is found

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return carExists;
    }


    public static List<Repair> getAllDefaultStatusRepairs() {
        String sql = "SELECT * FROM repair WHERE repair_status = 'IN PROGRESS'";
        List<Repair> repairList = new ArrayList<>();

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Repair repair = new Repair();
                repair.setRepairId(rs.getLong("repair_id"));  // Adjust attribute names based on table
                repair.setCarId(rs.getLong("car_id"));
                repair.setRepairDesc(rs.getString("repair_desc"));
                repair.setRepairDate(rs.getDate("repair_date"));
                // Add other attributes as needed

                repairList.add(repair);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return repairList;
}

public static boolean updateRepairDetails(Long repairId, Date newRepairDate, Long newEmployeeId, int newCost) {
    String sql = "UPDATE repair SET repair_date = ?, employee_id = ?, repair_status = ?, cost = ? WHERE repair_id = ?";

    try (Connection conn = DatabaseConnector.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        // Set parameters for the query
        pstmt.setDate(1, newRepairDate);               // Updating repair_date
        pstmt.setLong(2, newEmployeeId);                // Updating employee_id
        pstmt.setString(3, "PAYMENT DUE");             // Setting repair_status to "PAYMENT DUE"
        pstmt.setInt(4, newCost);                      // Updating cost
        pstmt.setLong(5, repairId);                     // Specifying which repair to update

        int rowsUpdated = pstmt.executeUpdate();
        return rowsUpdated > 0;   // Return true if at least one row was updated

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return false;  // Return false if update failed
}
}
