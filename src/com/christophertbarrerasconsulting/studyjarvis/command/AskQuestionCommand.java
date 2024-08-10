package com.christophertbarrerasconsulting.studyjarvis.command;

import com.christophertbarrerasconsulting.studyjarvis.Jarvis;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class AskQuestionCommand extends Command {
    AskQuestionCommand () {
        commandText = "ask-question";
        shortCut = "aq";
        helpText = "Ask Gemini a question.";
    }

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
        Scanner scanner = new Scanner(System.in);

        System.out.print("\n\nEnter your question: ");
        String question = scanner.nextLine();

        System.out.println(jarvis.askQuestion(question));
    }
}
