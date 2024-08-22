package com.christophertbarrerasconsulting.studyjarvis.server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PasswordHasherTest {
    @Test
    void hashPasswordHashes() {
        String password = "password";
        String hashedPassword = PasswordHasher.hashPassword(password);
        System.out.println(hashedPassword);
        Assertions.assertFalse(hashedPassword.isEmpty());
    }
}
