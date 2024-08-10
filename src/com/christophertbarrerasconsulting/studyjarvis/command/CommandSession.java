package com.christophertbarrerasconsulting.studyjarvis.command;

import java.io.IOException;
import java.util.Scanner;

public class CommandSession {
    public static String extractFolder = "C:\\slides";
    public static String bucketName = "chris_barreras_studyjarvis";
    public static String geminiProjectId = "studyjarvis";
    public static String geminiModelName = "gemini-1.5-pro";
    public static String geminiLocation = "us-west1";

    public static void start() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("> ");
        String commandText = scanner.nextLine();

        while (!commandText.equalsIgnoreCase("quit")) {
            Command command = CommandParser.parse(commandText);
            command.run();
            System.out.print("> ");
            commandText = scanner.nextLine();
        }
    }
}
