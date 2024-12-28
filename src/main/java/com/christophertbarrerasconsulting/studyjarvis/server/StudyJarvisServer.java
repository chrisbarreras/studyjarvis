package com.christophertbarrerasconsulting.studyjarvis.server;

import io.javalin.Javalin;
import io.javalin.plugin.bundled.CorsPluginConfig;

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
        app.post("/logout", LogoutHandler.getInstance());

        // Middleware to check JWT in Authorization header
        app.before("/secure/*", AuthorizationHandler.getInstance());

        // Makes sure that if you are using an admin path that you are admin
        app.before("/secure/admin/*", AdminAuthorizationHandler.getInstance());

        // User account creation
        app.post("/secure/admin/user", CreateUserHandler.getInstance());

        // Gets user
        app.get("/secure/admin/getuser", GetUserHandler.getInstance());

        // Deletes user
        app.delete("/secure/admin/deleteuser", DeleteUserHandler.getInstance());

        // Gets session
        app.get("/secure/admin/getsession", GetSessionHandler.getInstance());

        //Deletes session
        app.delete("/secure/admin/deletesession", DeleteSessionHandler.getInstance());

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
                ctx.status(500).result("Error");
            }
        });

        // Get a message
        app.get("/secure/GetMessage", ctx -> {
            ctx.result("This is a message from your Javalin server.");
        });

        // Send a message
        app.post("/secure/SendMessage", ctx -> {
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