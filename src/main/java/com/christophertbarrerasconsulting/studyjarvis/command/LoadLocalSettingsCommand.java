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
        Properties properties = ConfigReader.readProperties();

        CommandSession.bucketName = properties.getProperty(AppSettings.BucketName.toString());
        CommandSession.extractFolder = properties.getProperty(AppSettings.ExtractFolder.toString());
        CommandSession.geminiLocation = properties.getProperty(AppSettings.GeminiLocation.toString());
        CommandSession.geminiModelName = properties.getProperty(AppSettings.GeminiModelName.toString());
        CommandSession.geminiProjectId = properties.getProperty(AppSettings.GeminiProjectId.toString());

        Command displaySettings = new DisplayLocalSettingsCommand();
        displaySettings.run(new ArrayList<>());
    }
}
