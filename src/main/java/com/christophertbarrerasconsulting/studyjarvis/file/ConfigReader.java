package com.christophertbarrerasconsulting.studyjarvis.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    public static Properties readProperties() throws IOException {
        String appDataPath = AppConfigPath.getAppDataPath();
        Properties properties = new Properties();

        // Load properties file
        properties.load(new FileInputStream(appDataPath + "//studyjarvis.properties"));
        //System.out.println("Properties loaded from " + appDataPath + "/" + "studyjarvis.properties");

        return properties;
    }

    public static void saveProperties(Properties properties) throws IOException {
        String appDataPath = AppConfigPath.getAppDataPath();
        FileOutputStream out = new FileOutputStream(appDataPath + "//studyjarvis.properties");
        // Store properties in file
        properties.store(out, "Application Settings");
        System.out.println("Properties saved to " + appDataPath + "/" + "studyjarvis.properties");
    }
}

