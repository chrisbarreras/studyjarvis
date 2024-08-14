package com.christophertbarreras.studyjarvis.extraction;

import com.christophertbarrerasconsulting.studyjarvis.GoogleBucket;
import com.christophertbarrerasconsulting.studyjarvis.command.CommandSession;
import com.christophertbarrerasconsulting.studyjarvis.file.AppSettings;
import com.christophertbarrerasconsulting.studyjarvis.file.ConfigReader;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

public class GoogleBucketFunctionalTest {
    @org.junit.jupiter.api.Test
    void clearBucketClearsBucket () throws IOException {
        Properties properties = ConfigReader.readProperties();

        int numberOfItemsInBucketFirstCount = GoogleBucket.getInstance(properties.getProperty(AppSettings.BucketName.toString())).countBucket();
        GoogleBucket.getInstance(properties.getProperty(AppSettings.BucketName.toString())).uploadDirectoryContents(Path.of("src/functional/resources/files"));
        int numberOfItemsInBucketSecondCount = GoogleBucket.getInstance(properties.getProperty(AppSettings.BucketName.toString())).countBucket();

        Assertions.assertNotEquals(numberOfItemsInBucketFirstCount, numberOfItemsInBucketSecondCount);

        GoogleBucket.getInstance(properties.getProperty(AppSettings.BucketName.toString())).clearBucket();
        int numberOfItemsInBucketThirdCount = GoogleBucket.getInstance(properties.getProperty(AppSettings.BucketName.toString())).countBucket();

        Assertions.assertNotEquals(numberOfItemsInBucketSecondCount, numberOfItemsInBucketThirdCount);
    }
}
