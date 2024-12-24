package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.file.FileHandler;
import com.christophertbarrerasconsulting.studyjarvis.user.Session;
import com.christophertbarrerasconsulting.studyjarvis.user.User;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

import static com.christophertbarrerasconsulting.studyjarvis.Util.deleteUserIfExists;
import static org.junit.jupiter.api.Assertions.*;

public class SessionWriterFunctionalTest {
    @Test
    public void deleteSessionDeletesASession() throws IOException, SQLException {
        String username = UUID.randomUUID().toString();

        try {
            // Create a new admin test user
            User user = UserWriter.createNewUser(new User(username, "password", true));
            Session session = SessionWriter.createSession(user.getUserId());
            assertTrue(SessionWriter.deleteSession(session.getSessionId()));
            assertFalse(FileHandler.directoryExists((session.getUploadedFilesPath())), "Upload directory not deleted");
            assertFalse(FileHandler.directoryExists((session.getExtractFolder())), "Extract directory not deleted");

            Session deletedSession = SessionReader.getSession(session.getSessionId());
            assertNull(deletedSession);

            assertFalse(SessionWriter.deleteSession(session.getSessionId()));
        } finally {
            deleteUserIfExists(username);
        }
    }
}
