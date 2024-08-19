package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.user.User;
import io.javalin.Javalin;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Handler;
import io.javalin.plugin.bundled.CorsPluginConfig;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudyJarvisServer {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule((CorsPluginConfig.CorsRule::anyHost));
            });
        }).start(7000);

        // User account creation
        app.post("/CreateAccount", ctx -> {
            ObjectMapper mapper = new ObjectMapper();
            User user = mapper.readValue(ctx.body(), User.class);

            try (Connection conn = Database.connect()) {
                PreparedStatement checkStmt = conn.prepareStatement("SELECT 1 FROM users WHERE username = ?");
                checkStmt.setString(1, user.getUsername());
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    ctx.status(409).result("User already exists");
                    return;
                }
                String hashedPassword = PasswordHasher.hashPassword(user.getPassword());
                PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO users (username, password_hash) VALUES (?, ?)");
                insertStmt.setString(1, user.getUsername());
                insertStmt.setString(2, hashedPassword);
                insertStmt.executeUpdate();
                ctx.status(201).result("User created successfully");
            } catch (SQLException e) {
                e.printStackTrace();
                ctx.status(500).result("Database error");
            }
        });

        // User login
        app.post("/Login", LoginHandler.getInstance());

        // User logout
        app.post("/Logout", ctx -> {
            // JWT is stateless, just instruct client to discard the token
            ctx.result("Logged out successfully");
        });

        // Middleware to check JWT in Authorization header
        app.before("/secure/*", ctx -> {
            String authHeader = ctx.header("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                String username = JwtUtil.validateToken(token);
                if (username == null) {
                    ctx.status(401).result("Unauthorized");
                } else {
                    ctx.attribute("username", username); // Pass username to downstream handlers
                }
            } else {
                ctx.status(401).result("Unauthorized");
            }
        });

        // Upload files
        app.post("/secure/UploadFiles", ctx -> {
            String username = ctx.attribute("username");
            ctx.uploadedFiles("files").forEach(file -> {
                try (Connection conn = Database.connect()) {
                    PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO files (username, filename, filecontent) VALUES (?, ?)");
                    insertStmt.setString(1, username);
                    insertStmt.setString(2, file.filename());
                    insertStmt.setBlob(3, file.content());
                    insertStmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            ctx.status(200).result("Files uploaded successfully");
        });

        // Display uploaded files
        app.get("/secure/DisplayUploadedFiles", ctx -> {
            String username = ctx.attribute("username");
            try (Connection conn = Database.connect()) {
                PreparedStatement stmt = conn.prepareStatement("SELECT filename FROM files WHERE username = ?");
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                List<String> files = new ArrayList<>();
                while (rs.next()) {
                    files.add(rs.getString("filename"));
                }
                ctx.json(files);
            } catch (SQLException e) {
                e.printStackTrace();
                ctx.status(500).result("Database error");
            }
        });

        // Get a message
        app.get("/GetMessage", ctx -> {
            ctx.result("This is a message from your Javalin server.");
        });

        // Send a message
        app.post("/SendMessage", ctx -> {
            String message = ctx.body();
            ctx.result("Received your message: " + message);
        });
    }
}