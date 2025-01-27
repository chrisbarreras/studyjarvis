package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.Jarvis;
import com.christophertbarrerasconsulting.studyjarvis.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class JarvisAskQuestionHandler implements Handler {
    public static Handler getInstance() {
        return HandlerDecorator.getInstance(new JarvisAskQuestionHandler());
    }

    private static class QuestionConfiguration { public String question; }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        String username = context.attribute("username");
        User user = UserReader.getUser(username);
        int userId = user.getUserId();

        ObjectMapper mapper = new ObjectMapper();
        QuestionConfiguration config = mapper.readValue(context.body(), QuestionConfiguration.class);

        try (Jarvis jarvis = Jarvis.getInstance(userId)) {
            String answer = jarvis.askQuestion(config.question);
            context.json(answer);
        }
    }
}
