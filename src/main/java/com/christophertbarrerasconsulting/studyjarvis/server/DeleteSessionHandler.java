package com.christophertbarrerasconsulting.studyjarvis.server;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteSessionHandler implements Handler {

    public static DeleteSessionHandler getInstance() {
        return new DeleteSessionHandler();
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        String username = context.queryParam("username");

        if (username == null || username.isEmpty()) {
            context.status(400).result("Username parameter is required");
            return;
        }

        try (Connection conn = Database.connect()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM sessions WHERE username = ?");
            stmt.setString(1, username);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                context.status(200).result("Session deleted successfully");
            } else {
                context.status(404).result("Session not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            context.status(500).result("Database error");
        }
    }
}
