package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.user.LoginRequest;
import com.christophertbarrerasconsulting.studyjarvis.user.LoginResponse;
import com.christophertbarrerasconsulting.studyjarvis.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import io.javalin.openapi.*;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginHandler implements Handler {
    public static Handler getInstance() {
        return HandlerDecorator.getInstance(new LoginHandler());
    }

    @OpenApi(
            summary = "User Login",
            description = "Authenticates a user and returns a JWT token.",
            operationId = "login",
            path = "/login",
            methods = {HttpMethod.POST},
            requestBody = @OpenApiRequestBody(
                    description = "User login credentials",
                    required = true,
                    content = {
                            @OpenApiContent(from = LoginRequest.class, mimeType = ContentType.JSON)
                    }
            ),
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            description = "Login successful. Returns username, isAdmin flag.",
                            content = {@OpenApiContent(from = LoginResponse.class)}
                    ),
                    @OpenApiResponse(status = "401", description = "Invalid username or password"),
                    @OpenApiResponse(status = "500", description = "Internal server error")
            }
    )

    @Override
    public void handle(@org.jetbrains.annotations.NotNull Context context) throws Exception {
        try (Connection conn = Database.connect()) {

            ObjectMapper mapper = new ObjectMapper();
            LoginRequest loginRequest = mapper.readValue(context.body(), LoginRequest.class);
            System.out.println("Checking user " + loginRequest.getUsername());

            User storedUser = UserReader.getUser(loginRequest.getUsername());

            if (storedUser!=null && BCrypt.checkpw(loginRequest.getPassword(), storedUser.getPassword())) {
                SessionWriter.createSession(storedUser.getUserId());

                String token = JwtUtil.generateToken(loginRequest.getUsername());
                LoginResponse response = new LoginResponse(
                        storedUser.getUsername(),
                        storedUser.getIsAdministrator(),
                        token
                );
                context.json(response);
            } else {
                context.status(401).result("Invalid username or password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            context.status(500).result("Error");
        }
    }
}
