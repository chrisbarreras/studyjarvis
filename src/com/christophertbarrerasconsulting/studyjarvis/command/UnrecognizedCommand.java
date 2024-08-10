package com.christophertbarrerasconsulting.studyjarvis.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UnrecognizedCommand extends Command {
    @Override
    public void run(List<String> args) throws IOException {
        System.out.println("Unrecognized Command");

        Command command = new DisplayHelpCommand();
        command.run(new ArrayList<>());
    }
}
