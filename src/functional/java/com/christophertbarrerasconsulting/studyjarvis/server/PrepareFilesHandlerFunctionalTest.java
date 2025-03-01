package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.GoogleBucket;
import com.christophertbarrerasconsulting.studyjarvis.file.AppSettings;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

import static com.christophertbarrerasconsulting.studyjarvis.Util.createUser;
import static com.christophertbarrerasconsulting.studyjarvis.Util.deleteUserIfExists;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PrepareFilesHandlerFunctionalTest {
    private static Client client = new Client();
    private static StudyJarvisServer server;
    private static Path file1;
    private static Path file2;

    @BeforeAll
    public static void startServer() throws SQLException, URISyntaxException, IOException {
        server = new StudyJarvisServer();
        server.start(7070);
        createUser("TestUser", "password", false);

        URL resourceUrl = PrepareFilesHandlerFunctionalTest.class.getClassLoader().getResource("Arch Quiz 2 All Slides.pptx");
        file1 = Paths.get(resourceUrl.toURI());

        resourceUrl = PrepareFilesHandlerFunctionalTest.class.getClassLoader().getResource("FUS SFE Intro_Lecture No_12 SW Estimation.pdf");
        file2 = Paths.get(resourceUrl.toURI());
        client.login("TestUser", "password");
    }

    @AfterAll
    public static void stopServer() throws SQLException, IOException {
        GoogleBucket.getInstance(AppSettings.BucketName.getBucketName(), UserReader.getUser("TestUser").getUserId()).clearBucket();
        client.logout();
        server.stop();
        deleteUserIfExists("TestUser");
    }

    @Test
    public void prepareFilesHandlerPreparesFiles() throws IOException, SQLException {
        File[] files = {file1.toFile(), file2.toFile()};
        Request request = client.postRequest(files, "/secure/files");
        Response response = client.newCall(request).execute();
        response.body().close();

        request = client.postRequest(files, "/secure/files/prepare");
        response = client.newCall(request).execute();
        assertEquals(200, response.code());
        response.body().close();

        GoogleBucket googleBucket = GoogleBucket.getInstance(AppSettings.BucketName.getBucketName(), UserReader.getUser("TestUser").getUserId());
        assertEquals(20, googleBucket.countBucket());
    }
}
