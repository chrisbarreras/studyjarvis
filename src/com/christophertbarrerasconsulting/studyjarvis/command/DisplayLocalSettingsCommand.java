package com.christophertbarrerasconsulting.studyjarvis.command;

import com.christophertbarrerasconsulting.studyjarvis.file.AppSettings;

import java.io.IOException;
import java.util.List;

public class DisplayLocalSettingsCommand extends Command {
    DisplayLocalSettingsCommand() {
        commandText = "display-settings";
        shortCut = "ds";
        helpText = "Display local application settings.";
    }

    @Override
    public void run(List<String> args) throws IOException {
        System.out.println(AppSettings.BucketName + ": " + CommandSession.bucketName);
        System.out.println(AppSettings.ExtractFolder + ": " + CommandSession.extractFolder);
        System.out.println(AppSettings.GeminiLocation + ": " + CommandSession.geminiLocation);
        System.out.println(AppSettings.GeminiModelName + ": " + CommandSession.geminiModelName);
        System.out.println(AppSettings.GeminiProjectId + ": " + CommandSession.geminiProjectId);
    }
}
