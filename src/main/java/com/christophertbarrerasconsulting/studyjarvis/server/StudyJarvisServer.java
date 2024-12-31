package com.christophertbarrerasconsulting.studyjarvis.server;

import io.javalin.Javalin;
import io.javalin.plugin.bundled.CorsPluginConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StudyJarvisServer {
    Javalin app = null;
    private static final Logger logger = LoggerFactory.getLogger(StudyJarvisServer.class);

    public void start(int port){
        logger.info("Starting the Javalin application...");
        app = Javalin.create(config -> {
            config.requestLogger.http((ctx, executionTimeMs) -> {
                // Log each request (method, path, status, time)
                logger.info("{} {} -> {} (took {} ms)",
                        ctx.method(),
                        ctx.path(),
                        ctx.status(),
                        executionTimeMs
                );
            });
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule((CorsPluginConfig.CorsRule::anyHost));
            });
        }).start(port);

        app.exception(Exception.class, (e, ctx) -> {
            logger.error("An error occurred while handling request {}", ctx.path(), e);
            ctx.status(500).result("Internal Server Error.");
        });

        // User login
        app.post("/login", LoginHandler.getInstance());

        // User logout
        app.post("/logout", LogoutHandler.getInstance());

        // Middleware to check JWT in Authorization header
        app.before("/secure/*", AuthorizationHandler.getInstance());

        // Makes sure that if you are using an admin path that you are admin
        app.before("/secure/admin/*", AdminAuthorizationHandler.getInstance());

        // User account creation
        app.post("/secure/admin/users", CreateUserHandler.getInstance());

        // Gets user
        app.get("/secure/admin/users/{username}", GetUserHandler.getInstance());

        // Deletes user
        app.delete("/secure/admin/users/{username}", DeleteUserHandler.getInstance());

        // Gets session
        app.get("/secure/admin/sessions", GetSessionHandler.getInstance());

        //Deletes session
        app.delete("/secure/admin/sessions", DeleteSessionHandler.getInstance());

        // Upload files
        app.post("/secure/files", UploadFilesHandler.getInstance());

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