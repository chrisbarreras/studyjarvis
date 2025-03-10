package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.file.FileHandler;
import com.christophertbarrerasconsulting.studyjarvis.user.CreateUserRequest;
import com.christophertbarrerasconsulting.studyjarvis.user.Session;
import com.christophertbarrerasconsulting.studyjarvis.user.User;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

import static com.christophertbarrerasconsulting.studyjarvis.Util.deleteSessionIfExists;
import static com.christophertbarrerasconsulting.studyjarvis.Util.deleteUserIfExists;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SessionReaderFunctionalTest {
    @Test
    public void createSessionCreatesASession() throws SQLException, IOException {
        String username = UUID.randomUUID().toString();

        try {
            // Create a new admin test user
            User user = UserWriter.createNewUser(new CreateUserRequest(username, "password", true));
            Session session = null;
            try {
                session = SessionWriter.createSession(user.getUserId());

                assertNotNull(session);
                assertTrue(FileHandler.directoryExists((session.getUploadedFilesPath())), "Upload directory not created");
                assertTrue(FileHandler.directoryExists((session.getExtractFolder())), "Extract directory not created");

                assertNotNull(SessionReader.getSessionBySessionId(session.getSessionId()), "SessionReader did not get session");
            }finally {
                deleteSessionIfExists(session);
            }
        } finally {
            deleteUserIfExists(username);
        }
    }
}
