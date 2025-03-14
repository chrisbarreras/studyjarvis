package com.christophertbarrerasconsulting.studyjarvis.command;

import com.christophertbarrerasconsulting.studyjarvis.file.AppSettings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class EditLocalSettingsCommand extends Command
{
    EditLocalSettingsCommand () {
        commandText = "edit-settings";
        shortCut = "es";
        helpText = "Edit and save local application settings.";
    }

    private String getSetting(String setting, String currentValue) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(setting);
        if (currentValue != ""){
            System.out.print(" (" + currentValue + ")");
        }
        System.out.print(": ");
        String result = scanner.nextLine();

        if (Objects.equals(result, "")){
            result = currentValue;
        }

        return result;
    }

    @Override
    public void run(List<String> args) throws IOException {
        System.out.println("Enter new values for system settings:");

        CommandSession.extractFolder = getSetting(AppSettings.ExtractFolder.toString(), CommandSession.extractFolder);
        CommandSession.bucketName = getSetting(AppSettings.BucketName.toString(), CommandSession.bucketName);
        CommandSession.geminiProjectId = getSetting(AppSettings.GeminiProjectId.toString(), CommandSession.geminiProjectId);
        CommandSession.geminiModelName = getSetting(AppSettings.GeminiModelName.toString(), CommandSession.geminiModelName);
        CommandSession.geminiLocation = getSetting(AppSettings.GeminiLocation.toString(), CommandSession.geminiLocation);

        Command command = new SaveLocalSettingsCommand();
        command.run(new ArrayList<>());
    }
}
