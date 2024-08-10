package com.christophertbarrerasconsulting.studyjarvis.command;

import com.christophertbarrerasconsulting.studyjarvis.Jarvis;

import java.io.IOException;
import java.util.Objects;

public class CreateQuizCommand extends Command {
    CreateQuizCommand () {
        commandText = "create-quiz";
        shortCut = "cq";
        helpText = "Ask Gemini to create a quiz of a specific size.";
    }

    @Override
    public void run() throws IOException {
        System.out.println(Jarvis.getInstance().createQuiz(Integer.parseInt(args.get(0))));
    }
}
