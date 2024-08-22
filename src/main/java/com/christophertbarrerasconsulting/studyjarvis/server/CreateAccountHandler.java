package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateAccountHandler implements Handler {
    public static Handler getInstance() {
        return HandlerDecorator.getInstance(new CreateAccountHandler());
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(context.body(), User.class);

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
