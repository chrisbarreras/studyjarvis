package com.christophertbarrerasconsulting.studyjarvis.command;

import com.christophertbarrerasconsulting.studyjarvis.Jarvis;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class CreateQuizCommand extends Command {
    CreateQuizCommand () {
        commandText = "create-quiz";
        shortCut = "cq";
        helpText = "Ask Gemini to create a quiz of a specific size.";
    }

    @Override
    public void run(List<String> args) throws IOException {
        Integer numberOfQuestions = getNumberOfQuestions(args);
        if (numberOfQuestions == null) return;
        System.out.println(Jarvis.getInstance().createQuiz(numberOfQuestions));
    }

    private static Integer getNumberOfQuestions(List<String> args) {
        if (args.isEmpty()) {
            System.out.println("No number is given.");
            return null;
        }
        int numberOfQuestions = 0;
        try { numberOfQuestions = Integer.parseInt(args.get(0)); } catch (NumberFormatException e) {
            System.out.println(args.get(0) + " is not a valid number.");
            return null;
        }

        if ( numberOfQuestions <= 0) {
            System.out.println(args.get(0) + " must be greater than 0.");
            return null;
        }
        return numberOfQuestions;
    }
}
