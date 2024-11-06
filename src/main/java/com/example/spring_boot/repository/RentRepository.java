package com.example.spring_boot.repository;

import com.example.spring_boot.entity.Rent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Rent> rentRowMapper = new RowMapper<Rent>() {
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

    // Create new rent record
    public int save(Rent rent) {
        String sql = "INSERT INTO rent (start_date, end_date, model_id, renter_id, employee_id, transaction_id, rental_status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, rent.getStart_date(), rent.getEnd_date(), rent.getModel_id(),
                                   rent.getRenter_id(), rent.getEmployee_id(), rent.getTransaction_id(),
                                   rent.getRental_status());
    }

    // Find Rent by ID
    public Rent findById(Long rent_id) {
        String sql = "SELECT * FROM rent WHERE rent_id = ?";
        return jdbcTemplate.queryForObject(sql, rentRowMapper, rent_id);
    }

    // Update Rent record
    public int update(Rent rent) {
        String sql = "UPDATE rent SET start_date = ?, end_date = ?, model_id = ?, renter_id = ?, " +
                     "employee_id = ?, transaction_id = ?, rental_status = ? WHERE rent_id = ?";
        return jdbcTemplate.update(sql, rent.getStart_date(), rent.getEnd_date(), rent.getModel_id(),
                                   rent.getRenter_id(), rent.getEmployee_id(), rent.getTransaction_id(),
                                   rent.getRental_status(), rent.getRent_id());
    }

    // Delete Rent by ID
    public int deleteById(Long rent_id) {
        String sql = "DELETE FROM rent WHERE rent_id = ?";
        return jdbcTemplate.update(sql, rent_id);
    }

    // List all Rent records
    public List<Rent> findAll() {
        String sql = "SELECT * FROM rent";
        return jdbcTemplate.query(sql, rentRowMapper);
    }
    public int updateField(Long rentId, String fieldName, Object value) {
        String sql = "UPDATE rent SET " + fieldName + " = ? WHERE rent_id = ?";
        return jdbcTemplate.update(sql, value, rentId);
    }
}
