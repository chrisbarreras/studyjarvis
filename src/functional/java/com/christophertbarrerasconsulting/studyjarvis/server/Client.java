package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.file.FileHandler;
import okhttp3.*;
import org.eclipse.jetty.util.IO;
import org.junit.platform.engine.support.hierarchical.EngineExecutionContext;
import org.junit.platform.engine.support.hierarchical.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Client {
    private static final String BASE_URL = "http://localhost:7070";
    private static final Logger log = LoggerFactory.getLogger(Client.class);
    private final OkHttpClient client = new OkHttpClient();
    private String authorizationHeader = "";

    public void login() throws IOException {
        login("admin", "password");
    }

    public void login(String username, String password) throws IOException {
        String json = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);
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
                .header("Authorization", authorizationHeader)
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

    public Request postRequest(File[] files, String url){
        // Start building the multipart request
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        // Loop through your file array
        for (File file : files) {
            MediaType mediaType = FileHandler.getMediaType(file);
            RequestBody fileBody = RequestBody.create(file, mediaType);

            // Add each file to the multipart builder
            multipartBuilder.addFormDataPart(
                    "files",
                    file.getName(),
                    fileBody
            );
        }

        // Build the request body
        RequestBody requestBody = multipartBuilder.build();

        // Create the request
        return new Request.Builder()
                .header("Authorization", authorizationHeader)
                .url(BASE_URL + url)
                .post(requestBody)
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
