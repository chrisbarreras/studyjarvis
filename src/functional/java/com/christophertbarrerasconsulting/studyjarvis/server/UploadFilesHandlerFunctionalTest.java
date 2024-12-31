package com.christophertbarrerasconsulting.studyjarvis.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;

import static com.christophertbarrerasconsulting.studyjarvis.Util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UploadFilesHandlerFunctionalTest {
    private static Client client = new Client();
    private static StudyJarvisServer server;
    private static ObjectMapper mapper = new ObjectMapper();
    private static Path file1;
    private static Path file2;

    public static Path createTempFile(String text) throws IOException {
        // Create the temp file. Prefix must be at least 3 characters.
        Path tempPath = Files.createTempFile("uploadTest-", ".txt");
        System.out.println("Temp file created: " + tempPath.toAbsolutePath());

        // From Java 11 onwards, you can use writeString:
        Files.writeString(tempPath, text, StandardOpenOption.WRITE);

        return tempPath;
    }

    @BeforeAll
    public static void startServer() throws SQLException {
        server = new StudyJarvisServer();
        server.start(7070);
        createUser("TestUser", "password", false);
    }

    @AfterAll
    public static void stopServer() throws SQLException {
        server.stop();
        deleteUserIfExists("TestUser");
    }

    @BeforeEach
    public void beforeEach() throws IOException {
        file1 = createTempFile("Test File 1");
        file2 = createTempFile("Test File 2");
        client.login("TestUser", "password");
    }

    @AfterEach
    public void afterEach() throws IOException {
        Files.delete(file1);
        Files.delete(file2);
        client.logout();
    }

    @Test
    public void uploadFilesUploadsFiles() throws IOException {
        File[] files = {file1.toFile(), file2.toFile()};
        Request request = client.postRequest(files, "/secure/files");
        try (Response createResponse = client.newCall(request).execute()) {
            assertEquals(201, createResponse.code(), "Attempted to upload files with non admin user failed with response code: " + createResponse.code());
        }
    }
}
