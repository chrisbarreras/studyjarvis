package com.christophertbarrerasconsulting.studyjarvis.file;

public class AppConfigPath {
    public static String getAppDataPath() {
        String osName = System.getProperty("os.name").toLowerCase();
        String homeDir = System.getProperty("user.home");

        if (osName.contains("win")) {
            // Windows
            return System.getenv("APPDATA");
        } else if (osName.contains("mac")) {
            // macOS
            return homeDir + "/Library/Application Support";
        } else {
            // Linux and others
            return homeDir + "/.config";
        }
    }
}
