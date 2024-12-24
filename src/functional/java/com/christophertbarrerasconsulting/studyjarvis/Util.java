package com.christophertbarrerasconsulting.studyjarvis;

import com.christophertbarrerasconsulting.studyjarvis.file.FileHandler;
import com.christophertbarrerasconsulting.studyjarvis.server.Client;
import com.christophertbarrerasconsulting.studyjarvis.user.Session;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Util {
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
}
