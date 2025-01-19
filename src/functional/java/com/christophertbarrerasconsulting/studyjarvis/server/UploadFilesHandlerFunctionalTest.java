package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.user.Session;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;

import static com.christophertbarrerasconsulting.studyjarvis.Util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UploadFilesHandlerFunctionalTest {
    private static Client client = new Client();
    private static StudyJarvisServer server;
    private static Path file1;
    private static Path file2;

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
    public void beforeEach() throws IOException, URISyntaxException {
        URL resourceUrl = getClass().getClassLoader().getResource("Arch Quiz 2 All Slides.pptx");
        file1 = Paths.get(resourceUrl.toURI());

        resourceUrl = getClass().getClassLoader().getResource("FUS SFE Intro_Lecture No_12 SW Estimation.pdf");
        file2 = Paths.get(resourceUrl.toURI());
        client.login("TestUser", "password");
    }

    @AfterEach
    public void afterEach() throws IOException {
        client.logout();
    }

    @Test
    public void uploadFilesUploadsFiles() throws IOException, SQLException {
        File[] files = {file1.toFile(), file2.toFile()};
        Request request = client.postRequest(files, "/secure/files");
        try (Response createResponse = client.newCall(request).execute()) {
            assertEquals(201, createResponse.code(), "Attempted to upload files with non admin user failed with response code: " + createResponse.code());
        }

        Session session = SessionReader.getSession(UserReader.getUser("TestUser").getUserId());
        assertTrue(Path.of(session.getUploadedFilesPath()).resolve(file1.getFileName()).toFile().exists());
        assertTrue(Path.of(session.getUploadedFilesPath()).resolve(file2.getFileName()).toFile().exists());
    }

    @Test
    public void uploadFilesFailsIfNotLoggedIn() throws IOException, SQLException {
        client.logout();
        File[] files = {file1.toFile(), file2.toFile()};
        Request request = client.postRequest(files, "/secure/files");
        try (Response createResponse = client.newCall(request).execute()) {
            assertEquals(401, createResponse.code(), "Attempted to upload files when not logged in failed with response code: " + createResponse.code());
        }
    }
}
