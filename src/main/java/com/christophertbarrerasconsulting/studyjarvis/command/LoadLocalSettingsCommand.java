package com.christophertbarrerasconsulting.studyjarvis.command;

import com.christophertbarrerasconsulting.studyjarvis.file.AppSettings;
import com.christophertbarrerasconsulting.studyjarvis.file.ConfigReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class LoadLocalSettingsCommand extends Command{
    public LoadLocalSettingsCommand() {
        commandText = "load-settings";
        shortCut = "ls";
        helpText = "Load and display local application settings.";
    }

    @Override
    public void run(List<String> args) throws IOException {
        CommandSession.bucketName = AppSettings.BucketName.getBucketName();
        CommandSession.extractFolder = AppSettings.ExtractFolder.getExtractFolder();
        CommandSession.geminiLocation = AppSettings.GeminiLocation.getGeminiLocation();
        CommandSession.geminiModelName = AppSettings.GeminiModelName.getGeminiModelName();
        CommandSession.geminiProjectId = AppSettings.GeminiProjectId.getGeminiProjectId();

        Command displaySettings = new DisplayLocalSettingsCommand();
        displaySettings.run(new ArrayList<>());
    }
}
