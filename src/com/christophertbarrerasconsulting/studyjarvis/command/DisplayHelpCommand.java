package com.christophertbarrerasconsulting.studyjarvis.command;

import java.io.IOException;

public class DisplayHelpCommand extends Command {
    DisplayHelpCommand () {
        commandText = "help";
        shortCut = "h";
        helpText = "Display this help information";
    }

    @Override
    public void run() throws IOException {
        for (Command command: CommandParser.commands) {
            System.out.printf("%-25s %-10s %-10s\n", command.commandText, command.shortCut, command.helpText);
        }
    }
}
