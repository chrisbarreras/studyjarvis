package com.christophertbarrerasconsulting.studyjarvis.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetSessionHandler implements Handler {

    public static Handler getInstance() {
        return HandlerDecorator.getInstance(new GetSessionHandler());
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        String username = context.queryParam("username");

        if (username == null || username.isEmpty()) {
            context.status(400).result("Username parameter is required");
            return;
        }

        try (Connection conn = Database.connect()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT session_id FROM sessions WHERE username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String sessionId = rs.getString("session_id");
                context.json(new SessionResponse(sessionId));
            } else {
                context.status(404).result("Session not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            context.status(500).result("Database error");
        }
    }

    private static class SessionResponse {
        private final String sessionId;

        public SessionResponse(String sessionId) {
            this.sessionId = sessionId;
        }

        public String getSessionId() {
            return sessionId;
        }
    }
}
