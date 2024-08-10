package com.christophertbarrerasconsulting.studyjarvis.command;

import com.christophertbarrerasconsulting.studyjarvis.Jarvis;
import com.christophertbarrerasconsulting.studyjarvis.quiz.InteractiveQuizType;

import java.io.IOException;
import java.util.Objects;

public class CreateInteractiveQuizCommand extends Command {
    @Override
    public void run() throws IOException {
        if (Objects.equals(CommandSession.bucketName, "")){
            throw new IllegalArgumentException("Bucket name is empty.");
        }
        if (Objects.equals(CommandSession.geminiProjectId, "")){
            throw new IllegalArgumentException("Gemini project ID is empty.");
        }
        if (Objects.equals(CommandSession.geminiModelName, "")){
            throw new IllegalArgumentException("Gemini model name is empty.");
        }
        if (Objects.equals(CommandSession.geminiLocation, "")){
            throw new IllegalArgumentException("Gemini location is empty.");
        }

        Jarvis jarvis = new Jarvis(CommandSession.bucketName, CommandSession.geminiProjectId, CommandSession.geminiModelName, CommandSession.geminiLocation);
        try {
            System.out.println(jarvis.createInteractiveQuiz(InteractiveQuizType.valueOf(CommandParser.secondPartOfList.toUpperCase()), Integer.parseInt(CommandParser.thirdPartOfList)));
        } catch (
                IOException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }
}
