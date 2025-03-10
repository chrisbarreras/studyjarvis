package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.user.CreateUserRequest;
import com.christophertbarrerasconsulting.studyjarvis.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.openapi.*;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

public class CreateUserHandler implements Handler {
    public static Handler getInstance() {
        return HandlerDecorator.getInstance(new CreateUserHandler());
    }

    @OpenApi(
            summary = "Create User",
            description = "Creates a new user.",
            operationId = "createUser",
            path = "/secure/admin/users",
            methods = {HttpMethod.POST},
            requestBody = @OpenApiRequestBody(
                    required = true,
                    content = {
                            @OpenApiContent(from = CreateUserRequest.class, mimeType = ContentType.JSON)
                    }
            ),
            responses = {
                    @OpenApiResponse(status = "201", description = "User created successfully", content = {@OpenApiContent(from = String.class, mimeType = "text/plain")}),
                    @OpenApiResponse(status = "401", description = "Unauthorized", content = {@OpenApiContent(from = String.class, mimeType = "text/plain")}),
                    @OpenApiResponse(status = "403", description = "Forbidden", content = {@OpenApiContent(from = String.class, mimeType = "text/plain")}),
                    @OpenApiResponse(status = "409", description = "User already exists", content = {@OpenApiContent(from = String.class, mimeType = "text/plain")}),
                    @OpenApiResponse(status = "500", description = "Internal Server Error", content = {@OpenApiContent(from = String.class, mimeType = "text/plain")})
            }
    )

    @Override
    public void handle(@NotNull Context context) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        CreateUserRequest user = mapper.readValue(context.body(), CreateUserRequest.class);

        User storedUser = UserReader.getUser(user.getUsername());
        if (storedUser != null){
            context.status(409).result("User already exists");
            return;
        }
        try (Connection conn = Database.connect()) {
            UserWriter.createNewUser(user);
            context.status(201).result("User created successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            context.status(500).result("Error");
        }
    }
}
