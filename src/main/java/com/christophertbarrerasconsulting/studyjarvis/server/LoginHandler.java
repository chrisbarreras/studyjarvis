package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginHandler implements Handler {
    public static Handler getInstance() {
        return HandlerDecorator.getInstance(new LoginHandler());
    }
    @Override
    public void handle(@org.jetbrains.annotations.NotNull Context context) throws Exception {
        try (Connection conn = Database.connect()) {
            ObjectMapper mapper = new ObjectMapper();
            User user = mapper.readValue(context.body(), User.class);
            PreparedStatement stmt = conn.prepareStatement("SELECT password_hash FROM users WHERE username = ?");
            stmt.setString(1, user.getUsername());
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && BCrypt.checkpw(user.getPassword(), rs.getString("password_hash"))) {
                System.out.println("Checking user " + user.getUsername());
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
