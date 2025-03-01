package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.Jarvis;
import com.christophertbarrerasconsulting.studyjarvis.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.openapi.*;
import org.jetbrains.annotations.NotNull;

public class JarvisCreateQuizHandler implements Handler {
    public static Handler getInstance() {
        return HandlerDecorator.getInstance(new JarvisCreateQuizHandler());
    }

    public static class QuizConfiguration {
        private int numberOfQuestions;

        public int getNumberOfQuestions() {
            return numberOfQuestions;
        }
    }

    @OpenApi(
            summary = "Create Quiz",
            description = "Returns a quiz.",
            operationId = "createQuiz",
            path = "/secure/jarvis/create-quiz",
            methods = {HttpMethod.POST},
            requestBody = @OpenApiRequestBody(
                    content = {
                            @OpenApiContent(from = QuizConfiguration.class, mimeType = ContentType.JSON)
                    }
            ),
            responses = {
                    @OpenApiResponse(status = "200", description = "Returned a quiz"),
                    @OpenApiResponse(status = "401", description = "Unauthorized"),
                    @OpenApiResponse(status = "500", description = "Internal server error")
            }
    )

    @Override
    public void handle(@NotNull Context context) throws Exception {
        String username = context.attribute("username");
        User user = UserReader.getUser(username);
        int userId = user.getUserId();

        ObjectMapper mapper = new ObjectMapper();
        QuizConfiguration config = mapper.readValue(context.body(), QuizConfiguration.class);

        try (Jarvis jarvis = Jarvis.getInstance(userId)) {
            String quiz = jarvis.createQuiz(config.getNumberOfQuestions());
            context.json(quiz);
        }
    }
}
