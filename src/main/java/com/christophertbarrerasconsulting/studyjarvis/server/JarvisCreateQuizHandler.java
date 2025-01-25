package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.Jarvis;
import com.christophertbarrerasconsulting.studyjarvis.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class JarvisCreateQuizHandler implements Handler {
    public static Handler getInstance() {
        return HandlerDecorator.getInstance(new JarvisCreateQuizHandler());
    }

    private static class QuizConfiguration { public int numberOfQuestions; }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        String username = context.attribute("username");
        User user = UserReader.getUser(username);
        int userId = user.getUserId();

        ObjectMapper mapper = new ObjectMapper();
        QuizConfiguration config = mapper.readValue(context.body(), QuizConfiguration.class);

        try (Jarvis jarvis = Jarvis.getInstance(userId)) {
            String quiz = jarvis.createQuiz(config.numberOfQuestions);
            context.json(quiz);
        }
    }
}
