package com.christophertbarrerasconsulting.studyjarvis;

import com.christophertbarrerasconsulting.studyjarvis.command.CommandSession;
import com.christophertbarrerasconsulting.studyjarvis.file.AppSettings;
import com.christophertbarrerasconsulting.studyjarvis.file.ConfigReader;
import com.christophertbarrerasconsulting.studyjarvis.quiz.InteractiveQuiz;
import com.christophertbarrerasconsulting.studyjarvis.quiz.InteractiveQuizType;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class Jarvis implements AutoCloseable{
    public static Jarvis getInstance () throws IOException {
        if (Objects.equals(CommandSession.bucketName, "")){
            throw new IllegalArgumentException("Bucket name is empty.");
        }
        if (Objects.equals(CommandSession.geminiProjectId, "")){
            throw new IllegalArgumentException("Gemini project ID is empty.");
        }
        if (Objects.equals(CommandSession.geminiModelName, "")){
            throw new IllegalArgumentException("Gemini model name is empty.");
        }
        if (Objects.equals(CommandSession.geminiLocation, "")){
            throw new IllegalArgumentException("Gemini location is empty.");
        }

        return new Jarvis(AppSettings.BucketName.getBucketName(), AppSettings.GeminiProjectId.getGeminiProjectId(), AppSettings.GeminiModelName.getGeminiModelName(), AppSettings.GeminiLocation.getGeminiLocation());
    }

    Gemini gemini;
    GoogleBucket bucket;

    private Jarvis(String bucketName, String geminiProjectID, String geminiModelName, String geminiLocation) {
        gemini = new Gemini(geminiProjectID, geminiModelName, geminiLocation);
        bucket = GoogleBucket.getInstance(bucketName);
        gemini.initializeMultiModalInput(bucket.getURIs().toArray(new String[0]));
    }

    private Jarvis(String bucketName, String geminiProjectID, String geminiModelName, String geminiLocation, int userId) {
        gemini = new Gemini(geminiProjectID, geminiModelName, geminiLocation);
        bucket = GoogleBucket.getInstance(bucketName, userId);
        gemini.initializeMultiModalInput(bucket.getURIs().toArray(new String[0]));
    }

    public String createQuiz(int numberOfQuestions) throws IOException {
        return (new InteractiveQuiz(gemini, numberOfQuestions)).quizText;
    }

    public String askQuestion(String question) throws IOException {
        return gemini.textInput("Here are some notes:\n\n" + gemini.respond("Generate comprehensive notes on all of the topics.") + "\n\n" + question);
    }

    public String createComprehensiveNotes() throws IOException {
        return gemini.respond("Generate detailed, comprehensive notes on all of the topics.");
    }

    public String createKeyPoints() throws IOException {
        return gemini.respond("Generate key points on all of the topics.");
    }

    public String createStudyGuide() throws IOException {
        return gemini.respond("Generate a Q&A style study guide including all of the topics.");
    }

    public InteractiveQuiz createInteractiveQuiz(InteractiveQuizType quizType, int numberOfQuestions) throws IOException {
        return InteractiveQuiz.getQuiz(quizType, gemini, numberOfQuestions);
    }

    public void close() {
        gemini.close();
    }
}
