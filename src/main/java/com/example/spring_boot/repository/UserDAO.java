package com.example.spring_boot.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.example.spring_boot.config.*;
public class UserDAO {

    public static Long getUserIdByUsername(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";
        Long userId = null;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                userId = rs.getLong("id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userId;
    }
}
