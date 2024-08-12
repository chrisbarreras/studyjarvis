package com.christophertbarrerasconsulting.studyjarvis.command;

import com.christophertbarrerasconsulting.studyjarvis.file.AppSettings;
import com.christophertbarrerasconsulting.studyjarvis.file.ConfigReader;
import org.apache.poi.ss.usermodel.IconMultiStateFormatting;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class SaveLocalSettingsCommand extends Command {
    SaveLocalSettingsCommand () {
        commandText = "save-settings";
        shortCut = "ss";
        helpText = "Save local application settings.";
    }

    @Override
    public void run(List<String> args) throws IOException {
        Properties properties = new Properties();

        properties.setProperty(AppSettings.BucketName.toString(), CommandSession.bucketName);
        properties.setProperty(AppSettings.ExtractFolder.toString(), CommandSession.extractFolder);
        properties.setProperty(AppSettings.GeminiLocation.toString(), CommandSession.geminiLocation);
        properties.setProperty(AppSettings.GeminiModelName.toString(), CommandSession.geminiModelName);
        properties.setProperty(AppSettings.GeminiProjectId.toString(), CommandSession.geminiProjectId);

        ConfigReader.saveProperties(properties);
    }
}
