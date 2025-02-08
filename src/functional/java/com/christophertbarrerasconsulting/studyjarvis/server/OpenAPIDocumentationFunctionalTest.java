package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.GoogleBucket;
import com.christophertbarrerasconsulting.studyjarvis.file.AppSettings;
import com.christophertbarrerasconsulting.studyjarvis.user.User;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.sql.SQLException;

import static com.christophertbarrerasconsulting.studyjarvis.Util.createUser;
import static com.christophertbarrerasconsulting.studyjarvis.Util.deleteUserIfExists;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OpenAPIDocumentationFunctionalTest {
    private static Client client = new Client();
    private static StudyJarvisServer server;

    @BeforeAll
    public static void startServer() throws SQLException, URISyntaxException, IOException {
        server = new StudyJarvisServer();
        server.start(7070);
    }

    @AfterAll
    public static void stopServer() throws SQLException, IOException {
        server.stop();
        deleteUserIfExists("TestUser");
    }

    @Test
    public void OpenAPIDoc() throws IOException {
        Request request = client.getRequest("/openapi");
        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code());
            String body = response.body().string();
            System.out.println(body);
        }
    }
}
