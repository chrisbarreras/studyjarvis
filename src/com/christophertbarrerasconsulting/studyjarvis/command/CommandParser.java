package com.christophertbarrerasconsulting.studyjarvis.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class CommandParser {
    public static String secondPartOfList;
    public static String thirdPartOfList;

    private static List<Command> commands = new ArrayList<>() {{
        add(new DisplayLocalSettingsCommand());
        add(new EditLocalSettingsCommand());
        add(new LoadLocalSettingsCommand());
        add(new SaveLocalSettingsCommand());
        add(new DisplayHelpCommand());
        add(new ClearExtractFolderCommand());
        add(new ExtractFileCommand());
        add(new ClearBucketCommand());
        add(new UploadBucketCommand());
        add(new CreateQuizCommand());
        add(new AskQuestionCommand());
        add(new CreateComprehensiveNotesCommand());
        add(new CreateKeyPointsCommand());
        add(new CreateStudyGuideCommand());
        add(new CreateInteractiveQuizCommand());
    }};

    public static Command parse(String commandText){
        List<String> splitCommand = StringSplitter.splitStringBySpaceIgnoringQuotes(commandText);
        secondPartOfList = "";
        thirdPartOfList = "";
        if (splitCommand.size() >= 2) {
            secondPartOfList = splitCommand.get(1);
        }
        if (splitCommand.size() >= 3) {
            thirdPartOfList = splitCommand.get(2);
        }
        for (Command command: commands) {
            if (Objects.equals(command.commandText, splitCommand.get(0).toLowerCase()) || Objects.equals(command.shortCut, splitCommand.get(0).toLowerCase())) {
                return command;
            }
        }
        return new UnrecognizedCommand();
    }
}
