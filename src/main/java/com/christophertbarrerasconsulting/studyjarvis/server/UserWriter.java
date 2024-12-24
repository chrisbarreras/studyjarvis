package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserWriter {
    public static User createNewUser(User user) throws SQLException {
        try (Connection conn = Database.connect()) {
            String hashedPassword = PasswordHasher.hashPassword(user.getPassword());
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO users (username, password_hash, is_administrator) VALUES (?, ?, ?)");
            insertStmt.setString(1, user.getUsername());
            insertStmt.setString(2, hashedPassword);
            insertStmt.setBoolean(3, user.getIsAdministrator());
            insertStmt.executeUpdate();
            return UserReader.getUser(user.getUsername());
        }
    }
}
