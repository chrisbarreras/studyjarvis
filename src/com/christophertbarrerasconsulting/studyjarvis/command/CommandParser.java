package com.christophertbarrerasconsulting.studyjarvis.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class CommandParser {
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
        String firstCommand = splitCommand.get(0).toLowerCase();
        for (Command command: commands) {
            if (Objects.equals(command.commandText, firstCommand) || Objects.equals(command.shortCut, firstCommand)) {
                splitCommand.remove(0);
                command.setArgs(splitCommand);
                return command;
            }
        }
        return new UnrecognizedCommand();
    }
}
