package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.user.User;
import com.christophertbarrerasconsulting.studyjarvis.user.Session;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.openapi.*;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetSessionHandler implements Handler {

    public static Handler getInstance() {
        return HandlerDecorator.getInstance(new GetSessionHandler());
    }

    @OpenApi(
            summary = "Get Session",
            description = "Retrieves a user session based on the provided username.",
            operationId = "getSession",
            path = "/secure/admin/sessions",
            methods = {HttpMethod.GET},
            queryParams = {
                    @OpenApiParam(name = "username", description = "The username of the user whose session is being retrieved", required = true)
            },
            responses = {
                    @OpenApiResponse(status = "200", description = "Session retrieved successfully", content = {@OpenApiContent(from = Session.class)}),
                    @OpenApiResponse(status = "400", description = "Username is required", content = {@OpenApiContent(from = String.class, mimeType = "text/plain")}),
                    @OpenApiResponse(status = "401", description = "Unauthorized", content = {@OpenApiContent(from = String.class, mimeType = "text/plain")}),
                    @OpenApiResponse(status = "403", description = "Forbidden", content = {@OpenApiContent(from = String.class, mimeType = "text/plain")}),
                    @OpenApiResponse(status = "404", description = "User or session not found", content = {@OpenApiContent(from = String.class, mimeType = "text/plain")}),
                    @OpenApiResponse(status = "500", description = "Internal Server Error", content = {@OpenApiContent(from = String.class, mimeType = "text/plain")})
            }
    )

    @Override
    public void handle(@NotNull Context context) throws Exception {
        String username = context.queryParam("username");

        if (username == null || username.isEmpty()) {
            context.status(400).result("Username parameter is required");
            return;
        }

        User user = UserReader.getUser(username);
        if (user == null) {
            context.status(404).result("User not found");
            return;
        }

        try  {
            Session session = SessionReader.getSession(user.getUserId());
            if (session == null) {
                context.status(404).result("Session not found");
            } else {
                context.json(session);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            context.status(500).result("Error");
        }
    }
}
