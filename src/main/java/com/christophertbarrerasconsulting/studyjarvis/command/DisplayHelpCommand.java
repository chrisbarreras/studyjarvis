package com.christophertbarrerasconsulting.studyjarvis.command;

import java.io.IOException;
import java.util.List;

public class DisplayHelpCommand extends Command {
    DisplayHelpCommand () {
        commandText = "help";
        shortCut = "h";
        helpText = "Display this help information";
    }

    @Override
    public void run(List<String> args) throws IOException {
        for (Command command: CommandParser.getInstance().commands) {
            System.out.printf("%-25s %-10s %-10s\n", command.commandText, command.shortCut, command.helpText);
        }
    }
}
