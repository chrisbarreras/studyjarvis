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
import static org.junit.jupiter.api.Assertions.assertFalse;

public class GetUserFunctionalTest {
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
        try (Response response = client.newCall(request).execute()) {}
        client.logout();
    }

    @AfterEach
    public void afterEach() throws IOException {
        client.logout();
    }

    @Test
    public void getUserCantGetIfNotLoggedIn () throws SQLException, IOException {
        Request request = client.getRequest("/secure/admin/users/testuser");
        try (Response response = client.newCall(request).execute()) {
            assertEquals(401, response.code(), "Attempted to get without a user logged in failed with response code: " + response.code());
        }
    }

    @Test
    public void getUserCantGetIfNonAdminUser () throws SQLException, IOException {
        client.login();
        String username = UUID.randomUUID().toString();

        Request request = client.postRequest(String.format("{\"username\":\"%s\",\"password\":\"password\",\"is_administrator\":false}", username),
                "/secure/admin/users");
        try (Response response = client.newCall(request).execute()) {
            assertEquals(201, response.code());
        }
        client.logout();
        client.login(username, "password");

        request = client.getRequest("/secure/admin/users/testuser");
        try (Response response = client.newCall(request).execute()) {
            assertEquals(403, response.code(), "Attempted to get with non admin user failed with response code: " + response.code());
        }
    }

    @Test
    public void getUserGetsUser () throws IOException, SQLException {
        client.login();

        Request request = client.getRequest("/secure/admin/users/testuser");
        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code(), "Attempted to get with admin user failed with response code: " + response.code());
            assertNotNull(response.body(), "Invalid response body");
            String body = response.body().string();
            User user = mapper.readValue(body, User.class);
            assertEquals("testuser", user.getUsername());
            assertNull(user.getPassword());
            assertFalse(user.getIsAdministrator());
        }
    }
}
