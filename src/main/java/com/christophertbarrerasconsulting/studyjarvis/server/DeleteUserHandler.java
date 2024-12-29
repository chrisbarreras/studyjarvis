package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.user.Session;
import com.christophertbarrerasconsulting.studyjarvis.user.User;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DeleteUserHandler implements Handler {
    public static Handler getInstance() {
        return HandlerDecorator.getInstance(new DeleteUserHandler());
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        String username = context.pathParam("username");
        if (username.isEmpty()) {
            context.status(400).result("Username is required");
            return;
        }

        User user = UserReader.getUser(username);
        if (user != null) {
            int userId = user.getUserId();
            for (Session session : SessionReader.getSessions(userId)){
                SessionWriter.deleteSession(session.getSessionId());
            }
        }

        try (Connection conn = Database.connect()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE username = ?");
            stmt.setString(1, username);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                context.status(204).result("User deleted successfully");
            } else {
                context.status(404).result("User not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            context.status(500).result("Error");
        }
    }
}
