package com.christophertbarrerasconsulting.studyjarvis.server;

import okhttp3.*;
import org.junit.platform.engine.support.hierarchical.EngineExecutionContext;
import org.junit.platform.engine.support.hierarchical.Node;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Client {
    private static final String BASE_URL = "http://localhost:7070";
    private final OkHttpClient client = new OkHttpClient();
    private String authorizationHeader = "";

    public void login() throws IOException {
        String json = "{\"username\":\"admin\",\"password\":\"password\"}";
        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url("http://localhost:7070/login")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code());
            authorizationHeader = response.header("Authorization");
        }
    }

    public void logout() throws IOException {
        String json = "";
        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url("http://localhost:7070/logout")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code());
            authorizationHeader = "";
        }
    }

    public Request postRequest(String bodyJson, String url){
        RequestBody body = RequestBody.create(bodyJson, MediaType.get("application/json; charset=utf-8"));

        return new Request.Builder()
                .header("Authorization", authorizationHeader)
                .url(BASE_URL + url)
                .post(body)
                .build();
    }

    public Call newCall(Request request) {
        return client.newCall(request);
    }

    public Request getRequest(String url) {
        return new Request.Builder()
                .header("Authorization", authorizationHeader)
                .url(BASE_URL + url)
                .get()
                .build();
    }

    public Request deleteRequest(String url) {
        return new Request.Builder()
                .header("Authorization", authorizationHeader)
                .url(BASE_URL + url)
                .delete() // Use DELETE method instead of POST
                .build();
    }
}
