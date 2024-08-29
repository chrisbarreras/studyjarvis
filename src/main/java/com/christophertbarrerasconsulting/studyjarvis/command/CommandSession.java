package com.christophertbarrerasconsulting.studyjarvis.command;

import java.io.IOException;
import java.util.Scanner;

public class CommandSession {
    public static String extractFolder;
    public static String bucketName;
    public static String geminiProjectId;
    public static String geminiModelName;
    public static String geminiLocation;
    public static boolean quit = false;

    public static void start() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("> ");
        String commandText = scanner.nextLine();

        while (true) {
            CommandParser.getInstance().run(commandText);
            System.out.print("> ");
            commandText = scanner.nextLine();
            if (quit) {
                break;
            }
        }
    }
}
