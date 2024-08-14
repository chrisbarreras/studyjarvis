package com.christophertbarreras.studyjarvis.extraction;

import com.christophertbarrerasconsulting.studyjarvis.GoogleBucket;
import com.christophertbarrerasconsulting.studyjarvis.command.CommandSession;
import com.christophertbarrerasconsulting.studyjarvis.file.AppSettings;
import com.christophertbarrerasconsulting.studyjarvis.file.ConfigReader;

import java.io.IOException;
import java.util.Properties;

public class GoogleBucketFunctionalTest {
    @org.junit.jupiter.api.Test
    void clearBucketClearsBucket () throws IOException {
        Properties properties = ConfigReader.readProperties();
        GoogleBucket.getInstance(properties.getProperty(AppSettings.BucketName.toString())).clearBucket();
    }
}
