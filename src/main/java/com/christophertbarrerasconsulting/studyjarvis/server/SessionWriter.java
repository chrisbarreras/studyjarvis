package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.file.FileHandler;
import com.christophertbarrerasconsulting.studyjarvis.user.Session;
import com.christophertbarrerasconsulting.studyjarvis.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.sql.Date;
import java.util.List;

public class SessionWriter {
    public static void createSession(int userId) throws SQLException, IOException {
        try (Connection conn = Database.connect()) {
            Date now = Date.valueOf(LocalDateTime.now().toLocalDate());
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO sessions (user_id, uploaded_files_path, extract_folder, session_creation, last_session_activity) VALUES (?, ?, ?, ?, ?)");
            insertStmt.setInt(1, userId);
            insertStmt.setString(2, FileHandler.createNewTempFolder("upload_" + userId));
            insertStmt.setString(3,  FileHandler.createNewTempFolder("extract_" + userId));
            insertStmt.setDate(4, now);
            insertStmt.setDate(5, now);
            insertStmt.executeUpdate();
        }
    }

    public static int deleteSessions(String username) throws SQLException, IOException {
        User user = UserReader.getUser(username);
        int deletedCount = 0;

        if (user != null){
            int userId = user.getUserId();
            List<Session> sessions = SessionReader.getSessions(userId);
            for (Session session: sessions){
                FileHandler.clearDirectory(Path.of(session.getExtractFolder()));
                FileHandler.clearDirectory(Path.of(session.getUploadedFilesPath()));
            }
            try (Connection conn = Database.connect()) {
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM sessions WHERE user_id = ?");
                stmt.setInt(1, userId);
                deletedCount = stmt.executeUpdate();
            }
        }
        return deletedCount;
    }
}
