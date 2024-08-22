package com.christophertbarrerasconsulting.studyjarvis.server;

import java.sql.*;

import com.christophertbarrerasconsulting.studyjarvis.user.Session;

public class SessionReader {
    public static Session getSession(int userId) throws SQLException {
        try (Connection conn = Database.connect()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT session_id, uploaded_files_path, extract_folder, session_creation, last_session_activity FROM sessions WHERE user_id = ?");
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int sessionId = rs.getInt("session_id");
                String uploadedFilesPath = rs.getString("uploaded_files_path");
                String extractFolder = rs.getString("extract_folder");
                Date sessionCreationDate = rs.getDate("session_creation");
                Date lastSessionActivity = rs.getDate("last_session_activity");

                return new Session(sessionId, userId, uploadedFilesPath, extractFolder, sessionCreationDate, lastSessionActivity);
            } else {
                return null;
            }
        }
    }
}
