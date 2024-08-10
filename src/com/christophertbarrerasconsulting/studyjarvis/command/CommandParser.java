package com.christophertbarrerasconsulting.studyjarvis.command;

public class CommandParser {
    public static Command parse(String commandText){
        return switch (commandText.toLowerCase()) {
            case "display-settings" -> new DisplayLocalSettingsCommand();
            case "edit-settings" -> new EditLocalSettingsCommand();
            case "load-settings" -> new LoadLocalSettingsCommand();
            case "save-settings" -> new SaveLocalSettingsCommand();
            case "help" -> new DisplayHelpCommand();
            default -> new UnrecognizedCommand();
        };
    }
}
