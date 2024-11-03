package com.example.spring_boot.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import com.example.spring_boot.entity.*;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;
    
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Method to get User ID by username using JdbcTemplate
    public Long getIdByUsername(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, username);
    }
    public User getuserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        User u= jdbcTemplate.queryForObject(sql, new UserRowMapper(), username);

        String roleSql = "SELECT r.id, r.role_name FROM roles r INNER JOIN user_roles ur ON r.id = ur.role_id WHERE ur.user_id = ?;";
        List<Role> roles = jdbcTemplate.query(roleSql, new RoleRowMapper(), u.getId());
        
        // Set roles to the user
        u.setRoles(new HashSet<>(roles));
        return u;
    }
    // Save user method
    public boolean saveUser(User user) {
        try {
            String sql = "INSERT INTO users (username, password, enabled) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.isEnabled());
            
            Long userId = getIdByUsername(user.getUsername());
            user.setId(userId);
            
            saveUserRoles(user);  // Save roles using the new user ID
            return true;  // Success
        } catch (Exception e) {
            System.err.println("Error saving user: " + e.getMessage());
            return false;  // Failure
        }
    }

    // Method to save user roles
    private void saveUserRoles(User user) {
        String sql = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
        for (Role role : user.getRoles()) {
            jdbcTemplate.update(sql, user.getId(), role.getId());
        }
    }

    // Get user by ID method
    public User getUserById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new UserRowMapper(), id);
    }

    // Custom RowMapper for User object mapping
    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setEnabled(rs.getBoolean("enabled"));
            return user;
        }
    }

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
