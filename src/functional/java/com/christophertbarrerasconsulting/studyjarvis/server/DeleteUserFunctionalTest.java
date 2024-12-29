package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.file.FileHandler;
import com.christophertbarrerasconsulting.studyjarvis.user.Session;
import com.christophertbarrerasconsulting.studyjarvis.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

import static com.christophertbarrerasconsulting.studyjarvis.Util.deleteUserIfExists;
import static org.junit.jupiter.api.Assertions.*;

public class DeleteUserFunctionalTest {
    private static ObjectMapper mapper = new ObjectMapper();
    private static Client client = new Client();
    private static StudyJarvisServer server;
    String username;
    String json;

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
    public void beforeEach() throws IOException, SQLException {
        username = "testuser";
        json = "{\"username\":\"" + username + "\",\"password\":\"testpassword\",\"is_administrator\":false}";

        // Ensure the user does not already exist before attempting to create
        deleteUserIfExists(username);

        client.login();
        Request request = client.postRequest(json, "/secure/admin/users");
        try (Response createResponse = client.newCall(request).execute()) {}
        client.logout();
    }

    @AfterEach
    public void afterEach() throws IOException {
        client.logout();
    }

    @Test
    public void deleteUserCantDeleteIfNotLoggedIn () throws SQLException, IOException {
        Request request = client.deleteRequest("/secure/admin/users/testuser");
        try (Response createResponse = client.newCall(request).execute()) {
            assertEquals(401, createResponse.code(), "Attempted to delete with non admin user failed with response code: " + createResponse.code());
        }

        // Verify user not deleted
        User user = UserReader.getUser("testuser");
        assertNotNull(user, "testuser was deleted");
    }

    @Test
    public void deleteUserCantDeleteIfNonAdminUser () throws SQLException, IOException {
        client.login();
        String username = UUID.randomUUID().toString();

        Request request = client.postRequest(String.format("{\"username\":\"%s\",\"password\":\"password\",\"is_administrator\":false}", username),
                "/secure/admin/users");
        try (Response response = client.newCall(request).execute()) {
            assertEquals(201, response.code());
        }
        client.logout();
        client.login(username, "password");

        request = client.deleteRequest("/secure/admin/users/testuser");
        try (Response createResponse = client.newCall(request).execute()) {
            assertEquals(403, createResponse.code(), "Attempted to delete with non admin user failed with response code: " + createResponse.code());
        }

        // Verify user not deleted
        User user = UserReader.getUser("testuser");
        assertNotNull(user, "testuser was deleted");
    }

    @Test
    public void deleteUserDeletesUser () throws IOException, SQLException {
        client.login();

        Request request = client.deleteRequest("/secure/admin/users/testuser");
        try (Response createResponse = client.newCall(request).execute()) {
            assertEquals(204, createResponse.code(), "Attempted to delete with non admin user failed with response code: " + createResponse.code());
        }

        // Verify user deleted
        User user = UserReader.getUser("testuser");
        assertNull(user, "testuser was not deleted");
    }

    @Test
    public void deleteUserDeletesSession () throws IOException {
        client.login("testuser", "testpassword");
        client.login();

        Session session;
        Request request = client.getRequest("/secure/admin/sessions?username=testuser");
        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code(), "Failed to get session with status code: " + response.code());
            assertNotNull(response.body(), "Invalid response body");
            String body = response.body().string();
            session = mapper.readValue(body, Session.class);
        }

        request = client.deleteRequest("/secure/admin/users/testuser");
        try (Response createResponse = client.newCall(request).execute()) {
            assertEquals(204, createResponse.code(), "Attempted to delete with non admin user failed with response code: " + createResponse.code());
        }

        request = client.getRequest("/secure/admin/sessions?username=testuser");
        try (Response createResponse = client.newCall(request).execute()) {
            assertEquals(404, createResponse.code(), "Incorrect status code: " + createResponse.code());
        }

        assertFalse(FileHandler.directoryExists((session.getUploadedFilesPath())), "Upload directory not deleted");
        assertFalse(FileHandler.directoryExists((session.getExtractFolder())), "Extract directory not deleted");
    }
}
