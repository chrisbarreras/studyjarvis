package com.christophertbarreras.studyjarvis.extraction;

import com.christophertbarrerasconsulting.studyjarvis.extraction.PDFExtractor;
import com.christophertbarrerasconsulting.studyjarvis.extraction.PowerPointExtractor;
import com.christophertbarrerasconsulting.studyjarvis.file.FileHandler;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;

public class PowerPointExtractorFunctionalTest {
    String tempFolder = "";

    @org.junit.jupiter.api.BeforeEach
    void beforeEach() {
        // Get the system temp directory
        String tempDir = System.getProperty("java.io.tmpdir");

        // Generate a unique name using UUID
        String uniqueFolderName = "PowerPointFunctionalTest_" + UUID.randomUUID();

        // Create a new folder with the unique name within the temp directory
        File newFolder = new File(tempDir, uniqueFolderName);
        if (newFolder.mkdir()) {
            tempFolder = newFolder.getAbsolutePath();
        } else {
            throw new RuntimeException("Failed to create temp folder");
        }
    }

    @org.junit.jupiter.api.AfterEach
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

    @org.junit.jupiter.api.Test
    void extractExtracts() throws URISyntaxException, IOException {
        URL resourceUrl = getClass().getClassLoader().getResource("Arch Quiz 2 All Slides.pptx");

        Path powerPointPath = Paths.get(resourceUrl.toURI());

        PowerPointExtractor.extract(powerPointPath.toAbsolutePath().toString(), tempFolder);

        try (Stream<Path> files = Files.list(Path.of(tempFolder))){
            Assertions.assertEquals(10, files.count());
        }
    }
}
