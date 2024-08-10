package com.christophertbarrerasconsulting.studyjarvis.command;

import com.christophertbarrerasconsulting.studyjarvis.Jarvis;

import java.io.IOException;
import java.util.Objects;

public class CreateComprehensiveNotesCommand extends Command {
    CreateComprehensiveNotesCommand () {
        commandText = "create-notes";
        shortCut = "cn";
        helpText = "Ask Gemini to create notes off of information in the Google Bucket.";
    }

    @Override
    public void run() throws IOException {
        System.out.println(Jarvis.getInstance().createComprehensiveNotes());
    }
}
