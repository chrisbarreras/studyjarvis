package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.file.FileHandler;
import com.christophertbarrerasconsulting.studyjarvis.user.Session;
import com.christophertbarrerasconsulting.studyjarvis.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class SessionWriter {
    public static Session createSession(int userId) throws SQLException, IOException {
        try (Connection conn = Database.connect()) {
            Date now = Date.valueOf(LocalDateTime.now().toLocalDate());
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO sessions (user_id, uploaded_files_path, extract_folder, session_creation, last_session_activity) VALUES (?, ?, ?, ?, ?) RETURNING session_id");
            insertStmt.setInt(1, userId);
            String uploadFolder = FileHandler.createNewTempFolder("upload_" + userId);
            insertStmt.setString(2, uploadFolder);
            String extractFolder = FileHandler.createNewTempFolder("extract_" + userId);
            insertStmt.setString(3,  extractFolder);
            insertStmt.setDate(4, now);
            insertStmt.setDate(5, now);

            int sessionId;
            try (ResultSet rs = insertStmt.executeQuery()) {
                if (rs.next()) {
                    sessionId = rs.getInt(1);  // Get the session_id
                } else {
                    throw new SQLException("Creating session failed, no session_id obtained.");
                }
            }
            return new Session(sessionId, userId, uploadFolder, extractFolder, now, now);
        }
    }

    public static int deleteSessions(String username) throws SQLException, IOException {
        User user = UserReader.getUser(username);
        int deletedCount = 0;

        if (user != null){
            int userId = user.getUserId();
            List<Session> sessions = SessionReader.getSessions(userId);
            for (Session session: sessions){
                FileHandler.deletePathIfExists(Path.of(session.getExtractFolder()));
                FileHandler.deletePathIfExists(Path.of(session.getUploadedFilesPath()));
            }
            try (Connection conn = Database.connect()) {
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM sessions WHERE user_id = ?");
                stmt.setInt(1, userId);
                deletedCount = stmt.executeUpdate();
            }
        }
        return deletedCount;
    }

    public static boolean deleteSession(int sessionId) throws SQLException, IOException {
        int executed;
        Session session = SessionReader.getSessionBySessionId(sessionId);
        if (session != null) {
            FileHandler.deletePathIfExists(Path.of(session.getExtractFolder()));
            FileHandler.deletePathIfExists(Path.of(session.getUploadedFilesPath()));
            try (Connection conn = Database.connect()) {
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM sessions WHERE session_id = ?");
                stmt.setInt(1, sessionId);
                executed = stmt.executeUpdate();
            }
            return executed > 0;
        }
        return false;
    }
}
