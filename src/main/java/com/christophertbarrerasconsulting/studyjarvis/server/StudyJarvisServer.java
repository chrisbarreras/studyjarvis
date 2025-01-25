package com.christophertbarrerasconsulting.studyjarvis.server;

import io.javalin.Javalin;
import io.javalin.plugin.bundled.CorsPluginConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.jetty.server.Server;

public class StudyJarvisServer {
    Javalin app = null;
    private static final Logger logger = LoggerFactory.getLogger(StudyJarvisServer.class);

    public void start(int port){
        logger.info("Starting the Javalin application...");
        app = Javalin.create(config -> {
            config.jetty.modifyServer(server -> {
                // Limit maximum form content size to 10 MB (10_000_000 bytes)
                server.setAttribute("org.eclipse.jetty.server.Request.maxFormContentSize", 10_000_000);
            });
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

        // Prepare Files
        app.post("/secure/files/prepare", PrepareFilesHandler.getInstance());

//        app.post("/secure/jarvis/ask", JarvisAskQuestionHandler.getInstance()); TODO

        app.post("/secure/jarvis/create-quiz", JarvisCreateQuizHandler.getInstance());

//        app.post("/secure/jarvis/create-notes", JarvisCreateNotesHandler.getInstance()); TODO

        app.post("/secure/jarvis/create-study-guide", JarvisCreateStudyGuideHandler.getInstance());

//        app.post("/secure/jarvis/create-key-points", JarvisCreateKeyPointsHandler.getInstance()); TODO

//        // Display uploaded files
//        app.get("/secure/files", ctx -> {
//            String username = ctx.attribute("username");
//            try (Connection conn = Database.connect()) {
//                PreparedStatement stmt = conn.prepareStatement("SELECT filename FROM files WHERE username = ?");
//                stmt.setString(1, username);
//                ResultSet rs = stmt.executeQuery();
//                List<String> files = new ArrayList<>();
//                while (rs.next()) {
//                    files.add(rs.getString("filename"));
//                }
//                ctx.json(files);
//            } catch (SQLException e) {
//                e.printStackTrace();
//                ctx.status(500).result("Error");
//            }
//        });
    }

    public void stop(){
        app.stop();
    }

    public static void main(String[] args) {
        StudyJarvisServer server = new StudyJarvisServer();
        server.start(7000);
    }
}