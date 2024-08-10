package com.christophertbarrerasconsulting.studyjarvis.command;

import java.util.List;

public class CommandParser {
    public static String secondPartOfList;

    public static Command parse(String commandText){
        List<String> splitCommand = StringSplitter.splitStringBySpaceIgnoringQuotes(commandText);
        secondPartOfList = splitCommand.get(1);
        return switch (splitCommand.get(0).toLowerCase()) {
            case "display-settings", "ds" -> new DisplayLocalSettingsCommand();
            case "edit-settings", "es" -> new EditLocalSettingsCommand();
            case "load-settings", "ls" -> new LoadLocalSettingsCommand();
            case "save-settings", "ss" -> new SaveLocalSettingsCommand();
            case "help", "h" -> new DisplayHelpCommand();
            case "clear-extract-folder", "cf" -> new ClearExtractFolderCommand();
            case "extract-file", "ef" -> new ExtractFileCommand();
            case "clear-bucket", "cb" -> new ClearBucketCommand();
            case "upload-bucket", "ub" -> new UploadBucketCommand();
            case "create-quiz", "cq" -> new CreateQuizCommand();
            default -> new UnrecognizedCommand();
        };
    }
}
