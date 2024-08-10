package com.christophertbarrerasconsulting.studyjarvis.command;

import com.christophertbarrerasconsulting.studyjarvis.file.AppSettings;
import com.christophertbarrerasconsulting.studyjarvis.file.ConfigReader;

import java.io.IOException;
import java.util.Properties;

public class SaveLocalSettingsCommand extends Command {
    @Override
    public void run() throws IOException {
        Properties properties = new Properties();

        properties.setProperty(AppSettings.BucketName.toString(), CommandSession.bucketName);
        properties.setProperty(AppSettings.ExtractFolder.toString(), CommandSession.extractFolder);
        properties.setProperty(AppSettings.GeminiLocation.toString(), CommandSession.geminiLocation);
        properties.setProperty(AppSettings.GeminiModelName.toString(), CommandSession.geminiModelName);
        properties.setProperty(AppSettings.GeminiProjectId.toString(), CommandSession.geminiProjectId);

        ConfigReader.saveProperties(properties);
    }
}
