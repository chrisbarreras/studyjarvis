package com.christophertbarrerasconsulting.studyjarvis;

import com.christophertbarrerasconsulting.studyjarvis.server.Client;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Util {
    private static Client client = new Client();
    public static void deleteUserIfExists(String username) throws IOException {
        client.login();

        // Check if the user exists
        Request getRequest = client.getRequest("/secure/admin/getuser?username=" + username);

        try (Response getResponse = client.newCall(getRequest).execute()) {
            if (getResponse.code() == 200) {
                // User exists, so delete them
                Request deleteRequest = client.deleteRequest("/secure/admin/deleteuser?username=" + username);

                try (Response deleteResponse = client.newCall(deleteRequest).execute()) {
                    assertEquals(200, deleteResponse.code(), "Failed to delete existing user with status code: " + deleteResponse.code());
                }
            } else if (getResponse.code() != 404) {
                throw new IOException("Unexpected response code when checking user existence: " + getResponse.code());
            }
        }
    }
}
