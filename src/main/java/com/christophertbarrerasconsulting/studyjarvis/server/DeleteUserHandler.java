package com.christophertbarrerasconsulting.studyjarvis.server;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteUserHandler implements Handler {
    public static DeleteUserHandler getInstance() {
        return new DeleteUserHandler();
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        String username = context.queryParam("username");
        if (username == null) {
            context.status(400).result("Username is required");
            return;
        }

        try (Connection conn = Database.connect()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE username = ?");
            stmt.setString(1, username);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                context.status(200).result("User deleted successfully");
            } else {
                context.status(404).result("User not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            context.status(500).result("Database error");
        }
    }
}
