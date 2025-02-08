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

public class LoginHandler implements Handler {
    public static Handler getInstance() {
        return HandlerDecorator.getInstance(new LoginHandler());
    }

    @OpenApi(
            summary = "Login",
            operationId = "login",
            path = "/login",
            methods = HttpMethod.POST,
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = User.class)}),
                    @OpenApiResponse(status = "401", content = {@OpenApiContent(format = "Invalid username or password")}),
                    @OpenApiResponse(status = "500", content = {@OpenApiContent(format = "Error")})
            }
    )

    @Override
    public void handle(@org.jetbrains.annotations.NotNull Context context) throws Exception {
        try (Connection conn = Database.connect()) {

            ObjectMapper mapper = new ObjectMapper();
            User user = mapper.readValue(context.body(), User.class);
            System.out.println("Checking user " + user.getUsername());

            User storedUser = UserReader.getUser(user.getUsername());

            if (storedUser!=null && BCrypt.checkpw(user.getPassword(), storedUser.getPassword())) {
                SessionWriter.createSession(storedUser.getUserId());

                String token = JwtUtil.generateToken(user.getUsername());
                context.header("Authorization", "Bearer " + token);
                context.result("Login successful");
            } else {
                context.status(401).result("Invalid username or password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            context.status(500).result("Error");
        }
    }
}
