package com.christophertbarrerasconsulting.studyjarvis.command;

import com.christophertbarrerasconsulting.studyjarvis.Jarvis;
import com.christophertbarrerasconsulting.studyjarvis.quiz.ConsoleInteractiveQuizRunner;
import com.christophertbarrerasconsulting.studyjarvis.quiz.InteractiveQuiz;
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
        if (!validArgs(args)) return;
        InteractiveQuizType type = getInteractiveQuizType(args);
        if (type == null) return;

        Integer numberOfQuestions = getNumberOfQuestions(args);
        if (numberOfQuestions == null) return;

        try {
            ConsoleInteractiveQuizRunner consoleInteractiveQuizRunner = new ConsoleInteractiveQuizRunner();
            consoleInteractiveQuizRunner.runQuiz(Jarvis.getInstance().createInteractiveQuiz(type, numberOfQuestions));
        } catch (
                IOException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    private static Integer getNumberOfQuestions(List<String> args) {
        int numberOfQuestions = 0;
        try { numberOfQuestions = Integer.parseInt(args.get(1)); } catch (NumberFormatException e) {
            System.out.println(args.get(1) + " is not a valid number.");
            return null;
        }

        if ( numberOfQuestions <= 0) {
            System.out.println(args.get(1) + " must be greater than 0.");
            return null;
        }
        return numberOfQuestions;
    }

    private static boolean validArgs(List<String> args) {
        if (args.isEmpty()) {
            System.out.println("No number is given.");
            return false;
        }
        if (args.size() < 2) {
            System.out.println("Not enough args.");
            return false;
        }
        return true;
    }

    private static InteractiveQuizType getInteractiveQuizType(List<String> args) {
        InteractiveQuizType type;
        try { type = InteractiveQuizType.valueOf(args.get(0).toUpperCase()); } catch (IllegalArgumentException e) {
            System.out.println(args.get(0) + " is not a valid quiz type. Valid types are SHORT_ANSWER, MULTIPLE_CHOICE");
            return null;
        }
        return type;
    }
}
