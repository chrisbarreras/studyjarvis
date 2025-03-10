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

        @OpenApiExample("5")
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
                    content = @OpenApiContent(
                            from = QuizConfiguration.class,
                            mimeType = ContentType.JSON
                    ),
                    required = true
            ),
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            description = "Returned a quiz",
                            content = @OpenApiContent(
                                    mimeType = "text/plain",
                                    example = "## UML Questions:\n" +
                                            "\n" +
                                            "1. **What does UML stand for?**\n" +
                                            "2. **What is the primary purpose of using UML?**\n" +
                                            "3. **What does the acronym \"OO\" represent in the context of UML's development?**\n" +
                                            "4. **Which three researchers are credited with combining their work to create UML?**\n" +
                                            "5. **What do the acronyms BOOCH, OML, and OOSE stand for?**\n" +
                                            "\n" +
                                            "---\n" +
                                            "## Answers:\n" +
                                            "\n" +
                                            "1. **Unified Modeling Language** \n" +
                                            "2. **To visually represent a system from different perspectives using diagrams, particularly promoting Object Oriented design.**\n" +
                                            "3. **Object Oriented**\n" +
                                            "4. **Grady Booch, Jim Rumbaugh, and Ivar Jacobsen**\n" +
                                            "5. **BOOCH: Booch method, OML: Object Modeling Technique, OOSE: Object-oriented software engineering**"
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
        QuizConfiguration config = mapper.readValue(context.body(), QuizConfiguration.class);

        try (Jarvis jarvis = Jarvis.getInstance(userId)) {
            String quiz = jarvis.createQuiz(config.getNumberOfQuestions());
            context.json(quiz);
        }
    }
}
