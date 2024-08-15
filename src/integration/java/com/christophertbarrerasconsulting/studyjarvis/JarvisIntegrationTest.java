package com.christophertbarrerasconsulting.studyjarvis;

import com.christophertbarrerasconsulting.studyjarvis.extraction.PowerPointExtractor;
import com.christophertbarrerasconsulting.studyjarvis.file.AppSettings;
import com.christophertbarrerasconsulting.studyjarvis.file.ConfigReader;
import com.christophertbarrerasconsulting.studyjarvis.file.FileHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class JarvisIntegrationTest {
    String tempFolder = "";

    @BeforeEach
    void beforeEach() {
        // Get the system temp directory
        String tempDir = System.getProperty("java.io.tmpdir");

        // Generate a unique name using UUID
        String uniqueFolderName = "PowerPointIntegrationTest_" + UUID.randomUUID();

        // Create a new folder with the unique name within the temp directory
        File newFolder = new File(tempDir, uniqueFolderName);
        if (newFolder.mkdir()) {
            tempFolder = newFolder.getAbsolutePath();
        } else {
            throw new RuntimeException("Failed to create temp folder");
        }
    }

    @AfterEach
    void afterEach()
    {
        try {
            FileHandler.clearDirectory(Path.of(tempFolder));
            File folder = new File(tempFolder);
            if (!folder.delete()){
                throw new RuntimeException("Failed to delete temp folder " + tempFolder);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void studyPowerPoint () throws URISyntaxException, IOException {
        // Extract PowerPoint to temp folder
        URL resourceUrl = getClass().getClassLoader().getResource("FUS SFE Intro_Lecture No_5 SW Design.pptx");
        Path powerPointPath = Paths.get(resourceUrl.toURI());
        PowerPointExtractor.extract(powerPointPath.toAbsolutePath().toString(), tempFolder);

        // Clearing Google Bucket
        GoogleBucket.getInstance(AppSettings.BucketName.getBucketName()).clearBucket();

        // Upload to Google Bucket
        GoogleBucket.getInstance(AppSettings.BucketName.getBucketName()).uploadDirectoryContents(Path.of(tempFolder));

        // Ask Jarvis for key points
        String keyPoints = Jarvis.getInstance().createKeyPoints();
        System.out.println(keyPoints);
        Assertions.assertFalse(keyPoints.isEmpty());

        // Ask Jarvis a question
        String answer = Jarvis.getInstance().askQuestion("Is content coupling a type of tight coupling? Respond with only yes or no.");
        Assertions.assertEquals("yes", answer.toLowerCase().strip());
    }
}
