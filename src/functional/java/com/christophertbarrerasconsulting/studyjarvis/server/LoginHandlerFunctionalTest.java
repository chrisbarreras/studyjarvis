package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.user.User;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginHandlerFunctionalTest {

    @Test
    void testSuccessfulLogin() {
        JavalinTest.test((app, client) -> {
            // Set up the route
            app.post("/login", new LoginHandler());

            // Simulate a user and make a request
            User user = new User("testuser", "password");

            // Send a POST request to /login
            var response = client.post("/login")
                    .body(new ObjectMapper().writeValueAsString(user))
                    .asString();

            // Validate the response
            assertEquals(200, response.getStatus());
            assertTrue(response.getHeaders().containsKey("Authorization"));
            assertEquals("Login successful", response.getBody());
        });
    }

    @Test
    void testInvalidLogin() {
        JavalinTest.test((app, client) -> {
            // Set up the route
            app.post("/login", new LoginHandler());

            // Simulate a user with incorrect credentials
            User user = new User("testuser", "wrongpassword");

            // Send a POST request to /login
            var response = client.post("/login")
                    .body(new ObjectMapper().writeValueAsString(user))
                    .asString();

            // Validate the response
            assertEquals(401, response.getStatus());
            assertEquals("Invalid username or password", response.getBody());
        });
    }

    @Test
    void testDatabaseError() {
        JavalinTest.test((app, client) -> {
            // Set up the route
            app.post("/login", new LoginHandler() {
                @Override
                public void handle(Context ctx) throws Exception {
                    throw new SQLException("Simulated database error");
                }
            });

            // Simulate a user
            User user = new User("testuser", "password");

            // Send a POST request to /login
            var response = client.post("/login")
                    .body(new ObjectMapper().writeValueAsString(user))
                    .asString();

            // Validate the response
            assertEquals(500, response.getStatus());
            assertEquals("Database error", response.getBody());
        });
    }
}
