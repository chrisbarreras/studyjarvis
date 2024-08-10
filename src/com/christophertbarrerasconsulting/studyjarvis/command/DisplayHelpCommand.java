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
        System.out.println("help: Display this help information");
        System.out.println("display-settings: Display local application settings");
        System.out.println("edit-settings: Edit and save local application settings");
        System.out.println("load-settings: Load and display local application settings");
        System.out.println("save-settings: Save local application settings");
        System.out.println("quit: Quit the command session");
    }
}
