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
        System.out.println(Jarvis.getInstance().createStudyGuide());
    }
}
