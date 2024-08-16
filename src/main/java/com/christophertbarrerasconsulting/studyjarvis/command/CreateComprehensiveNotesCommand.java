package com.christophertbarrerasconsulting.studyjarvis.command;

import com.christophertbarrerasconsulting.studyjarvis.Jarvis;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class CreateComprehensiveNotesCommand extends Command {
    CreateComprehensiveNotesCommand () {
        commandText = "create-notes";
        shortCut = "cn";
        helpText = "Ask Gemini to create notes off of information in the Google Bucket.";
    }

    @Override
    public void run(List<String> args) throws IOException {
        try (Jarvis jarvis = Jarvis.getInstance()) {
            System.out.println(jarvis.createComprehensiveNotes());
        }
    }
}
