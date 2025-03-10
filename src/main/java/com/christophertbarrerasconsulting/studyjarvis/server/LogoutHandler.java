package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import io.javalin.openapi.HttpMethod;
import io.javalin.openapi.OpenApi;
import io.javalin.openapi.OpenApiContent;
import io.javalin.openapi.OpenApiResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LogoutHandler implements Handler {
    public static Handler getInstance() {
        return new LogoutHandler();
    }

    @OpenApi(
            summary = "Logout",
            description = "Logs a user out and invalidates their session.",
            operationId = "logout",
            path = "/logout",
            methods = {HttpMethod.POST},
            responses = {
                    @OpenApiResponse(status = "200", description = "Logout successful"),
                    @OpenApiResponse(status = "500", description = "Internal Server Error", content = {@OpenApiContent(from = String.class, mimeType = "text/plain")})
            }
    )

    @Override
    public void handle(@org.jetbrains.annotations.NotNull Context context) throws Exception {
        try  {
            SessionWriter.deleteSessions(AuthorizationHandler.getUsernameFromContext(context));
        } catch (SQLException e) {
            e.printStackTrace();
            context.status(500).result("Error");
        }
    }
}
