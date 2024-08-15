package com.christophertbarrerasconsulting.studyjarvis.command;

import java.io.IOException;
import java.util.List;

public class QuitCommand extends Command {
    QuitCommand() {
        commandText = "quit";
        shortCut = "q";
        helpText = "Exits the command.";
    }

    @Override
    public void run(List<String> args) throws IOException {
        CommandSession.quit = true;
    }
}
