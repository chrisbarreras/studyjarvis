package com.christophertbarrerasconsulting.studyjarvis.server;

import okhttp3.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateAccountHandlerFunctionalTest {
    private static Client client = new Client();
    private static final String BASE_URL = "http://localhost:7070";

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
                "/secure/createaccount");
        try (Response response = client.newCall(request).execute()) {
            assertEquals(401, response.code());
            assertTrue(response.body().string().contains("Unauthorized"));
        }
    }

    @Test
    void cantCreateDuplicateUser() throws IOException {
        client.login();

        Request request = client.postRequest("{\"username\":\"admin\",\"password\":\"password\",\"is_administrator\":true}",
                "/secure/createaccount");

        try (Response response = client.newCall(request).execute()) {
            assertEquals(409, response.code());
            assertTrue(response.body().string().contains("User already exists"));
        }
    }

    @Test
    void createUserAndVerifyThenDelete() throws IOException, InterruptedException {
        client.login();

        String username = "testuser";
        String json = "{\"username\":\"" + username + "\",\"password\":\"testpassword\",\"is_administrator\":false}";

        // Ensure the user does not already exist before attempting to create
        deleteUserIfExists(username);

        Request request = client.postRequest(json, "/secure/createaccount");

        try (Response createResponse = client.newCall(request).execute()) {
            assertEquals(201, createResponse.code(), "User creation failed with status code: " + createResponse.code());
            assertTrue(createResponse.body().string().contains("User created successfully"));
        }

        // Pause to ensure the database has processed the insert before we query
        Thread.sleep(1000); // Add a delay if the database is slow in reflecting changes

        // Verify the user was created
        request = client.getRequest("/secure/getuser?username=" + username);

        try (Response getResponse = client.newCall(request).execute()) {
            assertEquals(200, getResponse.code(), "User retrieval failed with status code: " + getResponse.code());
            String responseBody = getResponse.body().string();
            assertTrue(responseBody.contains(username));
        }

        // Delete the user
        deleteUserIfExists(username);

        // Confirm the user was deleted
        try (Response getResponseAfterDelete = client.newCall(request).execute()) {
            assertEquals(404, getResponseAfterDelete.code(), "User deletion failed or user still exists after deletion.");
        }
    }


    private void deleteUserIfExists(String username) throws IOException {
        // Check if the user exists
        Request getRequest = client.getRequest("/secure/getuser?username=" + username);

        try (Response getResponse = client.newCall(getRequest).execute()) {
            if (getResponse.code() == 200) {
                // User exists, so delete them
                Request deleteRequest = client.deleteRequest("/secure/deleteuser?username=" + username);

                try (Response deleteResponse = client.newCall(deleteRequest).execute()) {
                    assertEquals(200, deleteResponse.code(), "Failed to delete existing user with status code: " + deleteResponse.code());
                }
            } else if (getResponse.code() != 404) {
                throw new IOException("Unexpected response code when checking user existence: " + getResponse.code());
            }
        }
    }
}
