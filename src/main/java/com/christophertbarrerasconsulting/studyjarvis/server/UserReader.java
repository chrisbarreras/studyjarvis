package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserReader {
    static User getUser(String username) throws SQLException {
        try (Connection conn = Database.connect()) {
            System.out.println("Getting user with username: " + username);

            String query = "SELECT user_id, username, password_hash, is_administrator FROM users WHERE username = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return new User(
                                rs.getInt("user_id"),
                                rs.getString("username"),
                                rs.getString("password_hash"),
                                rs.getBoolean("is_administrator")
                        );
                    }
                }
            }
        }
        return null;
    }
}
