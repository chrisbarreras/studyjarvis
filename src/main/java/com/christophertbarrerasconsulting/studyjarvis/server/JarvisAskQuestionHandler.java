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

        @OpenApiExample("What is UML?")
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
                            @OpenApiContent(
                                    from = QuestionConfiguration.class,
                                    mimeType = ContentType.JSON
                            )
                    },
                    required = true
            ),
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            description = "Returned an answer",
                            content = @OpenApiContent(
                                    mimeType = "text/plain",
                                    example = "UML stands for Unified Modeling Language. It's essentially a visual blueprint language used in software development to:\n" +
                                            "\n" +
                                            "* **Visualize:** Create clear diagrams that represent a software system's components and their relationships.\n" +
                                            "* **Specify:** Define how these components interact and behave within the system.\n" +
                                            "* **Construct:** Guide the actual building of the software based on the modeled design.\n" +
                                            "* **Document:**  Provide easy-to-understand documentation of the system's design for future reference.\n" +
                                            "\n" +
                                            "Think of it like an architect's blueprint, but instead of a building, it's for software. It helps developers communicate ideas, plan the structure, and ensure everyone is on the same page throughout the development process. \n"
                            )
                    ),
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
