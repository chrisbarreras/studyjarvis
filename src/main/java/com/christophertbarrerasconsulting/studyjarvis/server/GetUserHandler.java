package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetUserHandler implements Handler {
    public static GetUserHandler getInstance() {
        return new GetUserHandler();
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        String username = context.queryParam("username");

        if (username == null || username.isEmpty()) {
            context.status(400).result("Username query parameter is required");
            return;
        }

        try (Connection conn = Database.connect()) {
            System.out.println("Getting user with username: " + username);

            String query = "SELECT username, password_hash, is_administrator FROM users WHERE username = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        User user = new User(
                                rs.getString("username"),
                                rs.getString("password_hash"),
                                rs.getBoolean("is_administrator")
                        );
                        context.json(user);
                    } else {
                        context.status(404).result("User not found");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            context.status(500).result("Database error");
        }
    }
}

