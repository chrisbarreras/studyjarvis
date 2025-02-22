package com.christophertbarrerasconsulting.studyjarvis.server;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.openapi.*;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteSessionHandler implements Handler {

    public static Handler getInstance() {
        return HandlerDecorator.getInstance(new DeleteSessionHandler());
    }

    @OpenApi(
            summary = "Delete Session",
            description = "Deletes all sessions for a specified username.",
            operationId = "deleteSession",
            path = "/secure/admin/sessions",
            methods = {HttpMethod.DELETE},
            queryParams = {
                    @OpenApiParam(name = "username", description = "The username whose session should be deleted", required = true)
            },
            responses = {
                    @OpenApiResponse(status = "204", description = "Session successfully deleted"),
                    @OpenApiResponse(status = "400", description = "Username is required"),
                    @OpenApiResponse(status = "401", description = "Unauthorized"),
                    @OpenApiResponse(status = "403", description = "Forbidden"),
                    @OpenApiResponse(status = "404", description = "User or session not found"),
                    @OpenApiResponse(status = "500", description = "Internal server error")
            }
    )


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
                context.status(204);
            } else {
                context.status(404).result("Session not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            context.status(500).result("Error");
        }
    }
}
