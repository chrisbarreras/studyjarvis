package com.christophertbarrerasconsulting.studyjarvis.command;

import com.christophertbarrerasconsulting.studyjarvis.Jarvis;

import java.io.IOException;
import java.util.Objects;

public class CreateStudyGuideCommand extends Command {
    CreateStudyGuideCommand () {
        commandText = "create-study-guide";
        shortCut = "sg";
        helpText = "Ask Gemini to create a study guide from what is in the Google Bucket.";
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
        System.out.println(jarvis.createStudyGuide());
    }
}
