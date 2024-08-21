package com.christophertbarrerasconsulting.studyjarvis.server;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginHandlerFunctionalTest {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String URL = "http://localhost:7070/login";

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
    void testSuccessfulLogin() throws Exception {
        String json = "{\"username\":\"admin\",\"password\":\"password\"}";
        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(URL)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code());
            assertTrue(response.body().string().contains("Login successful"));
        }
    }

    @Test
    void testFailedLogin() throws Exception {
        String json = "{\"username\":\"admin\",\"password\":\"pass\"}";
        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(URL)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            assertEquals(401, response.code());
        }
    }
}
