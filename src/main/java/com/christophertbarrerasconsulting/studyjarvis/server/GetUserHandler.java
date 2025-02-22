package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.openapi.*;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetUserHandler implements Handler {
    public static Handler getInstance() {
        return HandlerDecorator.getInstance(new GetUserHandler());
    }

    @OpenApi(
            summary = "Get User",
            description = "Retrieves user details by username.",
            operationId = "getUser",
            path = "/secure/admin/users/{username}",
            methods = {HttpMethod.GET},
            pathParams = {
                    @OpenApiParam(name = "username", description = "The username of the user to retrieve", required = true)
            },
            responses = {
                    @OpenApiResponse(status = "200", description = "User retrieved successfully", content = {@OpenApiContent(from = User.class)}),
                    @OpenApiResponse(status = "400", description = "Username is required"),
                    @OpenApiResponse(status = "401", description = "Unauthorized"),
                    @OpenApiResponse(status = "403", description = "Forbidden"),
                    @OpenApiResponse(status = "404", description = "User not found"),
                    @OpenApiResponse(status = "500", description = "Internal server error")
            }
    )


    @Override
    public void handle(@NotNull Context context) throws Exception {
        String username = context.pathParam("username");

        if (username.isEmpty()) {
            context.status(400).result("Username is required");
            return;
        }

        try {
            User user = UserReader.getUser(username);

            if (user == null) {
                context.status(404).result("User not found");
            }
            else {
                User returnedUser = new User(user.getUserId(), user.getUsername(), null, user.getIsAdministrator());
                context.json(returnedUser);
            }
        } catch (SQLException e){
            e.printStackTrace();
            context.status(500).result("Error");
        }
    }
}

