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

public class CreateAccountHandler implements Handler {
    public static CreateAccountHandler getInstance() {
        return new CreateAccountHandler();
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(context.body(), User.class);

        try (Connection conn = Database.connect()) {
            PreparedStatement checkStmt = conn.prepareStatement("SELECT 1 FROM users WHERE username = ?");
            checkStmt.setString(1, user.getUsername());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                context.status(409).result("User already exists");
                return;
            }
            String hashedPassword = PasswordHasher.hashPassword(user.getPassword());
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO users (username, password_hash) VALUES (?, ?)");
            insertStmt.setString(1, user.getUsername());
            insertStmt.setString(2, hashedPassword);
            insertStmt.executeUpdate();
            context.status(201).result("User created successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            context.status(500).result("Database error");
        }
    }
}
