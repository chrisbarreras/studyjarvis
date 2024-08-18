package com.christophertbarrerasconsulting.studyjarvis.utils;
import org.junit.jupiter.api.*;

public class SecretKeyGeneratorTest {

    @Test
    public void generateSecretKeyGenerates(){
        String key = SecretKeyGenerator.generateSecretKey();
        System.out.println("Secret key: " + key);
        Assertions.assertFalse(key.isEmpty());
    }
}
