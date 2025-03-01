package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.Jarvis;
import com.christophertbarrerasconsulting.studyjarvis.user.User;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.openapi.HttpMethod;
import io.javalin.openapi.OpenApi;
import io.javalin.openapi.OpenApiResponse;
import org.jetbrains.annotations.NotNull;

public class JarvisCreateKeyPointsHandler implements Handler {
    public static Handler getInstance() {
        return HandlerDecorator.getInstance(new JarvisCreateKeyPointsHandler());
    }

    @OpenApi(
            summary = "Create Key Points",
            description = "Returns key points on all topics.",
            operationId = "createKeyPoints",
            path = "/secure/jarvis/create-key-points",
            methods = {HttpMethod.POST},
            responses = {
                    @OpenApiResponse(status = "200", description = "Returned key points"),
                    @OpenApiResponse(status = "401", description = "Unauthorized"),
                    @OpenApiResponse(status = "500", description = "Internal server error")
            }
    )

    @Override
    public void handle(@NotNull Context context) throws Exception {
        String username = context.attribute("username");
        User user = UserReader.getUser(username);
        int userId = user.getUserId();

        try (Jarvis jarvis = Jarvis.getInstance(userId)) {
            String keyPoints = jarvis.createKeyPoints();
            context.json(keyPoints);
        }
    }
}
