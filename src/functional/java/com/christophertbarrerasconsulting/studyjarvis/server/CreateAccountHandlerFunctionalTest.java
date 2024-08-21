package com.christophertbarrerasconsulting.studyjarvis.server;

import okhttp3.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateAccountHandlerFunctionalTest {
    private static final OkHttpClient client = new OkHttpClient();
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

    @Test
    void cantCreateDuplicateUser() throws IOException {
        String json = "{\"username\":\"admin\",\"password\":\"password\",\"is_administrator\":true}";
        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(BASE_URL + "/createaccount")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            assertEquals(409, response.code());
            assertTrue(response.body().string().contains("User already exists"));
        }
    }

    @Test
    void createUserAndVerifyThenDelete() throws IOException, InterruptedException {
        String username = "testuser";
        String json = "{\"username\":\"" + username + "\",\"password\":\"testpassword\",\"is_administrator\":false}";

        // Ensure the user does not already exist before attempting to create
        deleteUserIfExists(username);

        // Create the user
        RequestBody createBody = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
        Request createRequest = new Request.Builder()
                .url(BASE_URL + "/createaccount")
                .post(createBody)
                .build();

        try (Response createResponse = client.newCall(createRequest).execute()) {
            assertEquals(201, createResponse.code(), "User creation failed with status code: " + createResponse.code());
            assertTrue(createResponse.body().string().contains("User created successfully"));
        }

        // Pause to ensure the database has processed the insert before we query
        Thread.sleep(1000); // Add a delay if the database is slow in reflecting changes

        // Verify the user was created
        Request getRequest = new Request.Builder()
                .url(BASE_URL + "/getuser?username=" + username)
                .get()
                .build();

        try (Response getResponse = client.newCall(getRequest).execute()) {
            assertEquals(200, getResponse.code(), "User retrieval failed with status code: " + getResponse.code());
            String responseBody = getResponse.body().string();
            assertTrue(responseBody.contains(username));
        }

        // Delete the user
        deleteUserIfExists(username);

        // Confirm the user was deleted
        try (Response getResponseAfterDelete = client.newCall(getRequest).execute()) {
            assertEquals(404, getResponseAfterDelete.code(), "User deletion failed or user still exists after deletion.");
        }
    }


    private void deleteUserIfExists(String username) throws IOException {
        // Check if the user exists
        Request getRequest = new Request.Builder()
                .url(BASE_URL + "/getuser?username=" + username)
                .get()
                .build();

        try (Response getResponse = client.newCall(getRequest).execute()) {
            if (getResponse.code() == 200) {
                // User exists, so delete them
                Request deleteRequest = new Request.Builder()
                        .url(BASE_URL + "/deleteuser?username=" + username)
                        .delete() // Use DELETE method instead of POST
                        .build();

                try (Response deleteResponse = client.newCall(deleteRequest).execute()) {
                    assertEquals(200, deleteResponse.code(), "Failed to delete existing user with status code: " + deleteResponse.code());
                }
            } else if (getResponse.code() != 404) {
                throw new IOException("Unexpected response code when checking user existence: " + getResponse.code());
            }
        }
    }

}
