package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.user.User;
import io.javalin.Javalin;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Handler;
import io.javalin.plugin.bundled.CorsPluginConfig;
import org.jetbrains.annotations.NotNull;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudyJarvisServer {
    Javalin app = null;
    public void start(int port){
        app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule((CorsPluginConfig.CorsRule::anyHost));
            });
        }).start(port);

        // User login
        app.post("/login", LoginHandler.getInstance());

        // User logout
        app.post("/logout", ctx -> {
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

        // User account creation
        app.post("/secure/createaccount", CreateAccountHandler.getInstance());

        // Gets user
        app.get("/secure/getuser", GetUserHandler.getInstance());

        // Deletes user
        app.delete("/secure/deleteuser", DeleteUserHandler.getInstance());

        // Gets session
        app.get("/secure/getsession", GetSessionHandler.getInstance());

        //Deletes session
        app.delete("/secure/deletesession", DeleteSessionHandler.getInstance());

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

    public void stop(){
        app.stop();
    }

    public static void main(String[] args) {
        StudyJarvisServer server = new StudyJarvisServer();
        server.start(7000);
    }
}