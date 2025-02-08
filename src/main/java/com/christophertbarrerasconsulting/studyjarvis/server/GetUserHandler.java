package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.user.User;
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

public class GetUserHandler implements Handler {
    public static Handler getInstance() {
        return HandlerDecorator.getInstance(new GetUserHandler());
    }

//    @OpenApi(
//            summary = "Get user",
//            operationId = "getUser",
//            path = "/secure/admin/users/{username}",
//            methods = HttpMethod.GET,
//            tags = {"User"},
//            responses = {
//                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = User.class)})
//            }
//    )
    @Override
    public void handle(@NotNull Context context) throws Exception {
        String username = context.pathParam("username");

        if (username.isEmpty()) {
            context.status(400).result("Username query parameter is required");
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

