package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.user.CreateUserRequest;
import com.christophertbarrerasconsulting.studyjarvis.user.User;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

import static com.christophertbarrerasconsulting.studyjarvis.Util.deleteUserIfExists;
import static org.junit.jupiter.api.Assertions.*;

public class UserWriterFunctionalTest {
    @Test
    public void userWriterCreatesAUser() throws SQLException, IOException {
        String username = UUID.randomUUID().toString();

        try {
            // Create a new admin test user
            User user = UserWriter.createNewUser(new CreateUserRequest(username, "password", true));

            assertNotNull(user);
            assertEquals(username, user.getUsername());

            User readUser = UserReader.getUser(username);

            assertNotNull(readUser);
            assertNotEquals("",readUser.getUsername());
            assertEquals(readUser.getUsername(), user.getUsername());
            assertNotEquals("",readUser.getPassword());
            assertEquals(readUser.getPassword(), user.getPassword());
            assertEquals(readUser.getUserId(), user.getUserId());
            assertEquals(readUser.getIsAdministrator(), user.getIsAdministrator());
        } finally {
            deleteUserIfExists(username);
        }
    }
}
