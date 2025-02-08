package com.christophertbarrerasconsulting.studyjarvis.server;

import io.javalin.Javalin;
import io.javalin.openapi.OpenApiContact;
import io.javalin.openapi.OpenApiInfo;
import io.javalin.openapi.OpenApiLicense;
import io.javalin.plugin.bundled.CorsPluginConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.javalin.openapi.plugin.OpenApiPlugin;
import io.javalin.openapi.plugin.OpenApiPluginConfiguration;
import io.javalin.openapi.plugin.redoc.ReDocConfiguration;
import io.javalin.openapi.plugin.redoc.ReDocPlugin;
import io.javalin.openapi.plugin.swagger.SwaggerConfiguration;
import io.javalin.openapi.plugin.swagger.SwaggerPlugin;
import io.javalin.apibuilder.*;

import static io.javalin.apibuilder.ApiBuilder.*;

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
            config.registerPlugin(new OpenApiPlugin(pluginConfig -> {
                pluginConfig.withDefinitionConfiguration((version, definition) -> {
                    //definition.withInfo(info -> info.setTitle("Javalin OpenAPI"));
                });
            }));
            config.registerPlugin(new SwaggerPlugin());
            config.registerPlugin(new ReDocPlugin());
//            config.router.apiBuilder(() -> {
//                path("login", () -> {
//                    post(LoginHandler.getInstance());
//                });
//            });
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

        // Ask Question
        app.post("/secure/jarvis/ask", JarvisAskQuestionHandler.getInstance());

        // Create Quiz
        app.post("/secure/jarvis/create-quiz", JarvisCreateQuizHandler.getInstance());

        // Create Nots
        app.post("/secure/jarvis/create-notes", JarvisCreateNotesHandler.getInstance());

        // Create Study Guide
        app.post("/secure/jarvis/create-study-guide", JarvisCreateStudyGuideHandler.getInstance());

        // Create Key Points
        app.post("/secure/jarvis/create-key-points", JarvisCreateKeyPointsHandler.getInstance());

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