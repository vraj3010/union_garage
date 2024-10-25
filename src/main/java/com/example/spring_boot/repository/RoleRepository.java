package com.example.spring_boot.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.example.spring_boot.entity.Role;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RoleRepository {

    private final JdbcTemplate jdbcTemplate;

    public RoleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Method to find a Role by roleName
    public Role findByRoleName(String roleName) {
        String sql = "SELECT * FROM roles WHERE role_name = ?";
        return jdbcTemplate.queryForObject(sql, new RoleRowMapper(), roleName);
    }

    // Method to find all roles
    public List<Role> findAll() {
        String sql = "SELECT * FROM roles";
        return jdbcTemplate.query(sql, new RoleRowMapper());
    }

    // Method to save a role
    public void save(Role role) {
        String sql = "INSERT INTO roles (role_name) VALUES (?)";
        jdbcTemplate.update(sql, role.getRoleName());
    }

    // Method to delete a role by ID
    public void deleteById(Long id) {
        String sql = "DELETE FROM roles WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // Custom RowMapper to map ResultSet to Role object
    private static class RoleRowMapper implements RowMapper<Role> {
        @Override
        public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
            Role role = new Role();
            role.setId(rs.getLong("id"));
            role.setRoleName(rs.getString("role_name"));
            return role;
        }
    }
}
