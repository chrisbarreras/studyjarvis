package com.christophertbarrerasconsulting.studyjarvis.server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PasswordHasherTest {
    @Test
    void hashPasswordHashes() {
        String password = "password";
        String hashedPassword = PasswordHasher.hashPassword(password);
        Assertions.assertEquals("$2a$10$vDL2a.LiSszA4cvpTxUkFOkbjQoI3xwgTWHSmQlKh5MSjV.0PwJYy", hashedPassword);
    }
}
