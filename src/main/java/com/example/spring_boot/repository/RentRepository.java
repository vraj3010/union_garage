package com.example.spring_boot.repository;

import com.example.spring_boot.config.DatabaseConnector;
import com.example.spring_boot.entity.Rent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.dao.DataAccessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Rent> rentRowMapper = new RowMapper<Rent>() {
        @Override
        public Rent mapRow(ResultSet rs, int rowNum) throws SQLException {
            Rent rent = new Rent();
            rent.setRent_id(rs.getLong("rent_id"));
            rent.setStart_date(rs.getDate("start_date"));
            rent.setEnd_date(rs.getDate("end_date"));
            rent.setModel_id(rs.getLong("model_id"));
            rent.setRenter_id(rs.getLong("renter_id"));
            rent.setEmployee_id(rs.getLong("employee_id"));
            rent.setTransaction_id(rs.getLong("transaction_id"));
            rent.setRental_status(rs.getString("rental_status"));
            return rent;
        }
    };

    public static boolean isModelAvailableForRent(Long modelId) {
        String sql = "SELECT rent_quantity FROM inventory WHERE model_id = ?";
        boolean isAvailable = false;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, modelId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int rentQuantity = rs.getInt("rent_quantity");
                isAvailable = rentQuantity > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isAvailable;
    }
    // Create new Rent record
    // public static int save(Rent rent) {
    //     String sql = "INSERT INTO rent (start_date, end_date, model_id, renter_id, employee_id, transaction_id, rental_status) " +
    //                  "VALUES (?, ?, ?, ?, ?, ?, ?)";
    //     return jdbcTemplate.update(sql, rent.getStart_date(), rent.getEnd_date(), rent.getModel_id(),
    //                                rent.getRenter_id(), rent.getEmployee_id(), rent.getTransaction_id(),
    //                                rent.getRental_status());
    // }

    // Find Rent by ID
    public Rent findById(Long rentId) {
        String sql = "SELECT * FROM rent WHERE rent_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, rentRowMapper, rentId);
        } catch (DataAccessException e) {
            // Returns null if no record is found, alternatively, you can throw an exception
            return null;
        }
    }

    // Update Rent record
    // public int update(Rent rent) {
    //     String sql = "UPDATE rent SET start_date = ?, end_date = ?, model_id = ?, renter_id = ?, " +
    //                  "employee_id = ?, transaction_id = ?, rental_status = ? WHERE rent_id = ?";
    //     return jdbcTemplate.update(sql, rent.getStart_date(), rent.getEnd_date(), rent.getModel_id(),
    //                                rent.getRenter_id(), rent.getEmployee_id(), rent.getTransaction_id(),
    //                                rent.getRental_status(), rent.getRent_id());
    // }

    // // Delete Rent by ID
    // public int deleteById(Long rentId) {
    //     String sql = "DELETE FROM rent WHERE rent_id = ?";
    //     return jdbcTemplate.update(sql, rentId);
    // }

    // List all Rent records
    // public List<Rent> findAll() {
    //     String sql = "SELECT * FROM rent";
    //     return jdbcTemplate.query(sql, rentRowMapper);
    // }

    // Update a specific field dynamically with field validation
    // public int updateField(Long rentId, String fieldName, Object value) {
    //     // Validate allowed fields for update
    //     List<String> allowedFields = List.of("start_date", "end_date", "model_id", "renter_id", 
    //                                          "employee_id", "transaction_id", "rental_status");
    //     if (!allowedFields.contains(fieldName)) {
    //         throw new IllegalArgumentException("Invalid field name for update.");
    //     }

    //     String sql = "UPDATE rent SET " + fieldName + " = ? WHERE rent_id = ?";
    //     return jdbcTemplate.update(sql, value, rentId);
    // }

    // Find rents by rental status
    // public List<Rent> findByStatus(String rentalStatus) {
    //     String sql = "SELECT * FROM rent WHERE rental_status = ?";
    //     return jdbcTemplate.query(sql, rentRowMapper, rentalStatus);
    // }

    // Find rents within a specific date range
    // public List<Rent> findByDateRange(java.util.Date startDate, java.util.Date endDate) {
    //     String sql = "SELECT * FROM rent WHERE start_date >= ? AND end_date <= ?";
    //     return jdbcTemplate.query(sql, rentRowMapper, new java.sql.Date(startDate.getTime()), new java.sql.Date(endDate.getTime()));
    // }

    // // Count active rentals for a specific model (useful for availability checks)
    // public int countActiveRentalsByModel(Long modelId) {
    //     String sql = "SELECT COUNT(*) FROM rent WHERE model_id = ? AND rental_status = 'Active'";
    //     return jdbcTemplate.queryForObject(sql, Integer.class, modelId);
    // }
    
}
