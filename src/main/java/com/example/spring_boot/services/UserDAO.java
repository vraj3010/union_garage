// package com.example.spring_boot.services;

// import org.springframework.jdbc.core.JdbcTemplate;
// import org.springframework.jdbc.core.RowMapper;
// import com.example.spring_boot.entity.*;
// import org.springframework.stereotype.Repository;
// import java.sql.ResultSet;
// import java.sql.SQLException;
// import java.util.HashSet;
// import java.util.Set;

// @Repository
// public class UserDAO {

//     private final JdbcTemplate jdbcTemplate;

//     public UserDAO(JdbcTemplate jdbcTemplate) {
//         this.jdbcTemplate = jdbcTemplate;
//     }

//     // Method to get User ID by username using JdbcTemplate
//     public Long getIdByUsername(String username) {
//         String sql = "SELECT id FROM users WHERE username = ?";
//         return jdbcTemplate.queryForObject(sql, Long.class, username);
//     }

//     // Save user method
//     public void saveUser(User user) {
//         String sql = "INSERT INTO users (username, password, enabled) VALUES (?, ?, ?)";
//         jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.isEnabled());
//         Long userId = getIdByUsername(user.getUsername());
//         user.setId(userId);
//         saveUserRoles(user);  // Save roles using the new user ID
//     }

//     // Method to save user roles
//     private void saveUserRoles(User user) {
//         String sql = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
//         for (Role role : user.getRoles()) {
//             jdbcTemplate.update(sql, user.getId(), role.getId());
//         }
//     }

//     // Get user by ID method
//     public User getUserById(Long id) {
//         String sql = "SELECT * FROM users WHERE id = ?";
//         return jdbcTemplate.queryForObject(sql, new UserRowMapper(), id);
//     }

//     // Custom RowMapper for User object mapping
//     private static class UserRowMapper implements RowMapper<User> {
//         @Override
//         public User mapRow(ResultSet rs, int rowNum) throws SQLException {
//             User user = new User();
//             user.setId(rs.getLong("id"));
//             user.setUsername(rs.getString("username"));
//             user.setPassword(rs.getString("password"));
//             user.setEnabled(rs.getBoolean("enabled"));
//             return user;
//         }
//     }
// }
