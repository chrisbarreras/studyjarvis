package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.Jarvis;
import com.christophertbarrerasconsulting.studyjarvis.user.User;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.openapi.*;
import org.jetbrains.annotations.NotNull;

public class JarvisCreateNotesHandler implements Handler {
    public static Handler getInstance() {
        return HandlerDecorator.getInstance(new JarvisCreateNotesHandler());
    }

    @OpenApi(
            summary = "Create Notes",
            description = "Returns comprehensive notes on all topics.",
            operationId = "createNote",
            path = "/secure/jarvis/create-notes",
            methods = {HttpMethod.POST},
            responses = {
                    @OpenApiResponse(status = "200", description = "Returned notes"),
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
            String notes = jarvis.createComprehensiveNotes();
            context.json(notes);
        }
    }
}
