package com.christophertbarrerasconsulting.studyjarvis.command;

import com.christophertbarrerasconsulting.studyjarvis.Jarvis;
import com.christophertbarrerasconsulting.studyjarvis.quiz.InteractiveQuizType;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class CreateInteractiveQuizCommand extends Command {
    CreateInteractiveQuizCommand () {
        commandText = "create-interactive-quiz";
        shortCut = "iq";
        helpText = "Ask Gemini to start an interactive quiz session.";
    }

    @Override
    public void run(List<String> args) throws IOException {

        try {
            System.out.println(Jarvis.getInstance().createInteractiveQuiz(InteractiveQuizType.valueOf(args.get(0).toUpperCase()), Integer.parseInt(args.get(1))));
        } catch (
                IOException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }
}
