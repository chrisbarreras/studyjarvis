package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.user.User;
import com.christophertbarrerasconsulting.studyjarvis.user.Session;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.openapi.HttpMethod;
import io.javalin.openapi.OpenApi;
import io.javalin.openapi.OpenApiContent;
import io.javalin.openapi.OpenApiResponse;
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
            description = "Gets a session.",
            operationId = "getSession",
            path = "/secure/admin/sessions",
            methods = HttpMethod.GET,
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = Session.class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(format = "Username is required")}),
                    @OpenApiResponse(status = "401", content = {@OpenApiContent(format = "Unauthorized")}),
                    @OpenApiResponse(status = "403", content = {@OpenApiContent(format = "Forbidden")}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(format = "User not found")}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(format = "Session not found")}),
                    @OpenApiResponse(status = "500", content = {@OpenApiContent(format = "Error")})
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
