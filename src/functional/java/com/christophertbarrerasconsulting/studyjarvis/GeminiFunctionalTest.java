package com.christophertbarrerasconsulting.studyjarvis;

import com.christophertbarrerasconsulting.studyjarvis.Gemini;
import com.christophertbarrerasconsulting.studyjarvis.file.AppSettings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class GeminiFunctionalTest {
    @Test
    void textInputResponse() throws IOException {
        try (Gemini gemini = new Gemini(AppSettings.GeminiProjectId.getGeminiProjectId(), AppSettings.GeminiModelName.getGeminiModelName(), AppSettings.GeminiLocation.getGeminiLocation())) {
            String test = gemini.textInput("Hello.");
            System.out.println(test);
            Assertions.assertFalse(test.isEmpty());
        }
    }

    @Test
    void respondRemembersConversation() throws IOException{
        try (Gemini gemini = new Gemini(AppSettings.GeminiProjectId.getGeminiProjectId(), AppSettings.GeminiModelName.getGeminiModelName(), AppSettings.GeminiLocation.getGeminiLocation())) {
            String response = gemini.respond("My name is Chris. Who are you?");
            System.out.println(response);
            response = gemini.respond("What is my name?");
            System.out.println(response);
            Assertions.assertTrue(response.contains("Chris"));
        }
    }
}
