package com.christophertbarrerasconsulting.studyjarvis;

import com.christophertbarrerasconsulting.studyjarvis.file.FileHandler;
import com.christophertbarrerasconsulting.studyjarvis.user.Session;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Util {
    static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static void deleteUserIfExists(String username) throws SQLException {
        try (Connection conn = TestDatabase.connect()) {
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM users WHERE username = ?");
            deleteStmt.setString(1, username);
            deleteStmt.execute();
        }
    }

    public static void deleteSessionIfExists(Session session) throws SQLException, IOException {
        if (session != null) {
            FileHandler.deletePathIfExists(session.getExtractFolder());
            FileHandler.deletePathIfExists(session.getUploadedFilesPath());
            try (Connection conn = TestDatabase.connect()) {
                PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM sessions WHERE session_id = ?");
                deleteStmt.setInt(1, session.getSessionId());
                deleteStmt.execute();
            }
        }
    }

    public static void createUser(String username, String password, boolean isAdmin) throws SQLException {
        deleteUserIfExists(username);
        try (Connection conn = TestDatabase.connect()) {
            String hashedPassword = hashPassword(password);
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO users (username, password_hash, is_administrator) VALUES (?, ?, ?)");
            insertStmt.setString(1, username);
            insertStmt.setString(2, hashedPassword);
            insertStmt.setBoolean(3, isAdmin);
            insertStmt.executeUpdate();
        }
    }
}
