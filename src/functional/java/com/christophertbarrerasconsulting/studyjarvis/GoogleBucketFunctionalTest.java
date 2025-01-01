package com.christophertbarrerasconsulting.studyjarvis;

import com.christophertbarrerasconsulting.studyjarvis.GoogleBucket;
import com.christophertbarrerasconsulting.studyjarvis.file.AppSettings;
import com.christophertbarrerasconsulting.studyjarvis.file.ConfigReader;
import com.christophertbarrerasconsulting.studyjarvis.file.FileHandler;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class GoogleBucketFunctionalTest {
    static String bucketName;
    static {
        try {
            bucketName = ConfigReader.readProperties().getProperty(AppSettings.BucketName.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    static Path myTestDir = Path.of("src/functional/resources/files");

    @Test
    public void bucketWithDefaultUser() throws IOException {
        GoogleBucket bucket = GoogleBucket.getInstance(bucketName);
        int numberOfItemsInBucketFirstCount = bucket.countBucket();

        bucket.uploadDirectoryContents(myTestDir);
        int numberOfItemsInBucketSecondCount = bucket.countBucket();

        assertNotEquals(numberOfItemsInBucketFirstCount, numberOfItemsInBucketSecondCount);

        bucket.clearBucket();

        assertNotEquals(numberOfItemsInBucketSecondCount, bucket.countBucket());
    }

    @Test
    public void bucketWithMultipleUsers() throws IOException {
        GoogleBucket bucket1 = GoogleBucket.getInstance(bucketName, 1);
        GoogleBucket bucket2 = GoogleBucket.getInstance(bucketName, 2);

        bucket2.clearBucket();
        bucket1.clearBucket();

        assertEquals(0, bucket1.countBucket());
        assertEquals(0, bucket2.countBucket());

        bucket1.uploadDirectoryContents(myTestDir);
        assertEquals(2, bucket1.countBucket());
        assertEquals(0, bucket2.countBucket());

        ArrayList<String> uris1 = bucket1.getURIs();
        assertEquals(2, uris1.size());
        assertTrue(uris1.get(0).contains(bucket1.prefix));
        assertTrue(uris1.get(1).contains(bucket1.prefix));

        bucket2.uploadDirectoryContents(myTestDir);
        assertEquals(2, bucket1.countBucket());
        assertEquals(2, bucket2.countBucket());

        ArrayList<String> uris2 = bucket2.getURIs();
        assertEquals(2, uris2.size());
        assertTrue(uris2.get(0).contains(bucket2.prefix));
        assertTrue(uris2.get(1).contains(bucket2.prefix));

        bucket2.clearBucket();
        assertEquals(2, bucket1.countBucket());
        assertEquals(0, bucket2.countBucket());

        bucket1.clearBucket();
        assertEquals(0, bucket1.countBucket());
        assertEquals(0, bucket2.countBucket());
    }
}
