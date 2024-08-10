package com.christophertbarrerasconsulting.studyjarvis.command;

import java.io.IOException;
import java.util.Scanner;

public class CommandSession {
    public static String extractFolder;
    public static String bucketName;
    public static String geminiProjectId;
    public static String geminiModelName;
    public static String geminiLocation;

    public static void start() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("> ");
        String commandText = scanner.nextLine();

        while (!commandText.equalsIgnoreCase("quit")) {
            CommandParser.run(commandText);
            System.out.print("> ");
            commandText = scanner.nextLine();
        }
    }
}
