package com.christophertbarrerasconsulting.studyjarvis.command;

public class CommandParser {
    public static Command parse(String commandText){
        return switch (commandText.toLowerCase()) {
            case "display-settings", "ds" -> new DisplayLocalSettingsCommand();
            case "edit-settings", "es" -> new EditLocalSettingsCommand();
            case "load-settings", "ls" -> new LoadLocalSettingsCommand();
            case "save-settings", "ss" -> new SaveLocalSettingsCommand();
            case "help", "h" -> new DisplayHelpCommand();
            case "clear-extract-folder", "cf" -> new ClearExtractFolderCommand();
            default -> new UnrecognizedCommand();
        };
    }
}
