package com.christophertbarrerasconsulting.studyjarvis.command;

import com.christophertbarrerasconsulting.studyjarvis.Jarvis;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class CreateKeyPointsCommand extends Command {
    CreateKeyPointsCommand () {
        commandText = "create-key-points";
        shortCut = "kp";
        helpText = "Ask Gemini to create key points off of what is in the Google Bucket.";
    }

    @Override
    public void run(List<String> args) throws IOException {
        try (Jarvis jarvis = Jarvis.getInstance()) {
            System.out.println(jarvis.createKeyPoints());
        }
    }
}
