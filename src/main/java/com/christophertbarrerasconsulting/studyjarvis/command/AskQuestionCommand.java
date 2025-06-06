package com.christophertbarrerasconsulting.studyjarvis.command;

import com.christophertbarrerasconsulting.studyjarvis.Jarvis;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class AskQuestionCommand extends Command {
    AskQuestionCommand () {
        commandText = "ask-question";
        shortCut = "aq";
        helpText = "Ask Gemini a question.";
    }

    @Override
    public void run(List<String> args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter your question: ");
        String question = scanner.nextLine();

        try (Jarvis jarvis = Jarvis.getInstance()) {
            System.out.println(jarvis.askQuestion(question));
        }
    }
}
