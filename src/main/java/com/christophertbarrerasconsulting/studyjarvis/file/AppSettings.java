package com.christophertbarrerasconsulting.studyjarvis.file;

import java.io.IOException;

public enum AppSettings {
    ExtractFolder, BucketName, GeminiProjectId, GeminiModelName, GeminiLocation;

    public String getBucketName () throws IOException {
        return ConfigReader.readProperties().getProperty(AppSettings.BucketName.toString());
    }
    public String getGeminiProjectId () throws IOException {
        return ConfigReader.readProperties().getProperty(AppSettings.GeminiProjectId.toString());
    }
    public String getExtractFolder () throws IOException {
        return ConfigReader.readProperties().getProperty(AppSettings.ExtractFolder.toString());
    }
    public String getGeminiModelName () throws IOException {
        return ConfigReader.readProperties().getProperty(AppSettings.GeminiModelName.toString());
    }
    public String getGeminiLocation () throws IOException {
        return ConfigReader.readProperties().getProperty(AppSettings.GeminiLocation.toString());
    }
}
