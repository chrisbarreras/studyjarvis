package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.file.FileHandler;
import com.christophertbarrerasconsulting.studyjarvis.user.Session;
import com.christophertbarrerasconsulting.studyjarvis.user.User;

import static com.christophertbarrerasconsulting.studyjarvis.Util.deleteUserIfExists;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.runtime.ObjectMethods;
import java.sql.SQLException;
import java.util.UUID;

import java.io.IOException;
import java.util.concurrent.ForkJoinPool;

import static org.junit.jupiter.api.Assertions.*;

public class SessionFunctionalTest {
    private static Client client = new Client();
    private static StudyJarvisServer server;
    private static ObjectMapper mapper = new ObjectMapper();

    @BeforeAll
    public static void startServer() {
        server = new StudyJarvisServer();
        server.start(7070);
    }

    @AfterAll
    public static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void beforeEach() throws IOException {
        client.login();
    }

    @Test
    public void LoginCreatesASession() throws IOException {
        Request request = client.getRequest("/secure/admin/getsession?username=admin");
        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code());
            String body = response.body().string();
            Session session = mapper.readValue(body, Session.class);
            assertTrue(FileHandler.directoryExists((session.getUploadedFilesPath())), "Upload directory not created");
            assertTrue(FileHandler.directoryExists((session.getExtractFolder())), "Extract directory not created");
        }
    }

    @Test
    public void LogoutRemovesASession() throws IOException, SQLException {
        String username = UUID.randomUUID().toString();

        try {
            // Create a new admin test user
            UserWriter.createNewUser(new User(username, "password", true));
            client.login(username, "password");

            // Get session info
            Request request = client.getRequest("/secure/admin/getsession?username=" + username);
            try (Response response = client.newCall(request).execute()) {
                assertEquals(200, response.code());
                String body = response.body().string();
                Session session = mapper.readValue(body, Session.class);

                // Logout
                client.logout();

                // Check whether the session and its folders were deleted
                Session storedSession = SessionReader.getSession(session.getUserId());
                assertNull(storedSession, "Session not deleted");

                assertFalse(FileHandler.directoryExists((session.getUploadedFilesPath())), "Upload directory not deleted");
                assertFalse(FileHandler.directoryExists((session.getExtractFolder())), "Extract directory not deleted");
            }
        } finally {
            deleteUserIfExists(username);
        }
    }
}
