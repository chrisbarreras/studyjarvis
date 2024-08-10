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
        System.out.println(Jarvis.getInstance().createQuiz(Integer.parseInt(args.get(0))));
    }
}
