package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.Jarvis;
import com.christophertbarrerasconsulting.studyjarvis.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.openapi.*;
import org.jetbrains.annotations.NotNull;

public class JarvisAskQuestionHandler implements Handler {
    public static Handler getInstance() {
        return HandlerDecorator.getInstance(new JarvisAskQuestionHandler());
    }

    public static class QuestionConfiguration {
        private String question;

        public String getQuestion() {
            return question;
        }
    }

    @OpenApi(
            summary = "Ask Question",
            description = "Returns an answer.",
            operationId = "askQuestion",
            path = "/secure/jarvis/ask",
            methods = {HttpMethod.POST},
            requestBody = @OpenApiRequestBody(
                    content = {
                            @OpenApiContent(from = QuestionConfiguration.class, mimeType = ContentType.JSON)
                    }
            ),
            responses = {
                    @OpenApiResponse(status = "200", description = "Returned an answer"),
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
        QuestionConfiguration config = mapper.readValue(context.body(), QuestionConfiguration.class);

        try (Jarvis jarvis = Jarvis.getInstance(userId)) {
            String answer = jarvis.askQuestion(config.getQuestion());
            context.json(answer);
        }
    }
}
