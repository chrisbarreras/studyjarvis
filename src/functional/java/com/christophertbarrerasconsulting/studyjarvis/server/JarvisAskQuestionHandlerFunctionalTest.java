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

public class JarvisAskQuestionHandlerFunctionalTest {
    private static Client client = new Client();
    private static StudyJarvisServer server;
    private static User user;
    private static Path myTestDir = Path.of("src/functional/resources/files");

    @BeforeAll
    public static void startServer() throws SQLException, URISyntaxException, IOException {
        server = new StudyJarvisServer();
        server.start(7070);
        createUser("TestUser", "password", false);
        client.login("TestUser", "password");
        user = UserReader.getUser("TestUser");
        GoogleBucket.getInstance(AppSettings.BucketName.getBucketName(), user.getUserId()).clearBucket();
        GoogleBucket.getInstance(AppSettings.BucketName.getBucketName(), user.getUserId()).uploadDirectoryContents(myTestDir);
    }

    @AfterAll
    public static void stopServer() throws SQLException, IOException {
        GoogleBucket.getInstance(AppSettings.BucketName.getBucketName(), user.getUserId()).clearBucket();
        client.logout();
        server.stop();
        deleteUserIfExists("TestUser");
    }

    @Test
    public void askQuestionCreatesResponse() throws IOException {
        Request request = client.postRequest("{\"question\":\"What is UML?\"}", "/secure/jarvis/ask");
        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code());
            String body = response.body().string();
            System.out.println(body);
            assertTrue(body.contains("UML"));
        }
    }
}
