package com.christophertbarrerasconsulting.studyjarvis.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class CommandParser {
    private static class CommandParserSingleton {
        private static final CommandParser instance = new CommandParser(new ArrayList<>() {{
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
            add(new CountBucketCommand());
        }}, new UnrecognizedCommand());
    }
    public static CommandParser getInstance(){
        return CommandParserSingleton.instance;
    }

    public CommandParser (List<Command> commands, Command unrecognizedCommand){
        this.commands.addAll(commands);
        this.unrecognizedCommand = unrecognizedCommand;
    }

    public final List<Command> commands = new ArrayList<>();
    private Command unrecognizedCommand = null;

    public void run(String commandText) throws IOException {
        List<String> splitCommand = StringSplitter.splitStringBySpaceIgnoringQuotes(commandText);
        String firstCommand = splitCommand.get(0).toLowerCase();
        for (Command command: commands) {
            if (Objects.equals(command.commandText, firstCommand) || Objects.equals(command.shortCut, firstCommand)) {
                splitCommand.remove(0);
                command.run(splitCommand);
                return;
            }
        }
        unrecognizedCommand.run(new ArrayList<>());
    }
}
