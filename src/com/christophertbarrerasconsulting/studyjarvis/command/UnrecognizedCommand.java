package com.christophertbarrerasconsulting.studyjarvis.command;

import java.io.IOException;

public class UnrecognizedCommand extends Command {
    @Override
    public void run() throws IOException {
        System.out.println("Unrecognized Command");

        Command command = new DisplayHelpCommand();
        command.run();
    }
}
