package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.user.User;
import okhttp3.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.UUID;

import java.io.IOException;

import static com.christophertbarrerasconsulting.studyjarvis.Util.deleteUserIfExists;
import static org.junit.jupiter.api.Assertions.*;

public class CreateAccountHandlerFunctionalTest {
    private static Client client = new Client();
    private static StudyJarvisServer server;

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
        client.logout();
    }

    @Test
    void cantCreateUserIfNotLoggedIn() throws IOException {
        Request request = client.postRequest(String.format("{\"username\":\"%s\",\"password\":\"password\",\"is_administrator\":true}", UUID.randomUUID()),
                "/secure/admin/createaccount");
        try (Response response = client.newCall(request).execute()) {
            assertEquals(401, response.code());
            assertTrue(response.body().string().contains("Unauthorized"));
        }
    }

    @Test
    void cantCreateUserIfNotAdmin() throws IOException, SQLException {
        client.login();
        String username = UUID.randomUUID().toString();
        String username2 = UUID.randomUUID().toString();
        try {
            Request request = client.postRequest(String.format("{\"username\":\"%s\",\"password\":\"password\",\"is_administrator\":false}", username),
                    "/secure/admin/createaccount");
            try (Response response = client.newCall(request).execute()) {
                assertEquals(201, response.code());
            }
            client.logout();

            client.login(username, "password");
            request = client.postRequest(String.format("{\"username\":\"%s\",\"password\":\"password\",\"is_administrator\":false}", username2),
                    "/secure/admin/createaccount");
            try (Response response = client.newCall(request).execute()) {
                assertEquals(403, response.code());
                assertTrue(response.body().string().contains("Forbidden"));
            }
        } finally {
            deleteUserIfExists(username);
            deleteUserIfExists(username2);
        }
    }

    @Test
    void cantCreateDuplicateUser() throws IOException {
        client.login();

        Request request = client.postRequest("{\"username\":\"admin\",\"password\":\"password\",\"is_administrator\":true}",
                "/secure/admin/createaccount");

        try (Response response = client.newCall(request).execute()) {
            assertEquals(409, response.code());
            assertTrue(response.body().string().contains("User already exists"));
        }
    }

    @Test
    void createUserAndVerifyThenDelete() throws IOException, InterruptedException, SQLException {
        client.login();

        String username = "testuser";
        String json = "{\"username\":\"" + username + "\",\"password\":\"testpassword\",\"is_administrator\":false}";

        // Ensure the user does not already exist before attempting to create
        deleteUserIfExists(username);

        Request request = client.postRequest(json, "/secure/admin/createaccount");

        try (Response createResponse = client.newCall(request).execute()) {
            assertEquals(201, createResponse.code(), "User creation failed with status code: " + createResponse.code());
            assertTrue(createResponse.body().string().contains("User created successfully"));
        }

        // Verify the user was created
        User user = UserReader.getUser("testuser");
        assertNotNull(user, "testuser was not created");
        assertEquals("testuser", user.getUsername(), "Wrong username");
        assertFalse(user.getIsAdministrator(), "User should be non-admin");

        // Delete the user
        deleteUserIfExists(username);

        // Verify user deleted
        user = UserReader.getUser("testuser");
        assertNull(user, "testuser was not deleted");

        // Try with an admin user
        json = "{\"username\":\"" + username + "\",\"password\":\"testpassword\",\"is_administrator\":true}";
        request = client.postRequest(json, "/secure/admin/createaccount");

        try (Response createResponse = client.newCall(request).execute()) {
            assertEquals(201, createResponse.code(), "User creation failed with status code: " + createResponse.code());
            assertTrue(createResponse.body().string().contains("User created successfully"));
        }

        // Verify the user was created
        user = UserReader.getUser("testuser");
        assertNotNull(user, "testuser was not created");
        assertEquals("testuser", user.getUsername(), "Wrong username");
        assertTrue(user.getIsAdministrator(), "User should be admin");
    }
}
