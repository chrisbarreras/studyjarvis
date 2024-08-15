package com.christophertbarreras.studyjarvis.extraction;

import com.christophertbarrerasconsulting.studyjarvis.Gemini;
import com.christophertbarrerasconsulting.studyjarvis.Jarvis;
import com.christophertbarrerasconsulting.studyjarvis.command.CommandParser;
import com.christophertbarrerasconsulting.studyjarvis.command.CommandSession;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;

import static com.christophertbarrerasconsulting.studyjarvis.quiz.InteractiveQuizType.SHORT_ANSWER;

public class JarvisFunctionalTest {
    @org.junit.jupiter.api.Test
    void createQuizCreatesQuiz () throws IOException {
        CommandParser.getInstance().run("ls");
        String quiz = Jarvis.getInstance().createQuiz(10);
        Assertions.assertFalse(quiz.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void createQuizCreatesCorrectQuizLength () throws IOException {
        CommandParser.getInstance().run("ls");
        String quiz = Jarvis.getInstance().createQuiz(10);
        Gemini gemini = new Gemini(CommandSession.geminiProjectId, CommandSession.geminiModelName, CommandSession.geminiLocation);
        String isLengthTen = gemini.textInput("Is this quiz ten questions long? Answer with only \"yes\" or \"no\": " + quiz);
        gemini.close();
        Assertions.assertEquals("yes", isLengthTen.toLowerCase().strip());
    }

    @org.junit.jupiter.api.Test
    void askQuestionReturns () throws IOException {
        CommandParser.getInstance().run("ls");
        String response = Jarvis.getInstance().askQuestion("Give me a test response.");
        Assertions.assertFalse(response.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void createComprehensiveNotesReturns () throws IOException {
        CommandParser.getInstance().run("ls");
        String response = Jarvis.getInstance().createComprehensiveNotes();
        Assertions.assertFalse(response.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void createKeyPointsReturns () throws IOException {
        CommandParser.getInstance().run("ls");
        String response = Jarvis.getInstance().createKeyPoints();
        Assertions.assertFalse(response.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void createStudyGuideReturns () throws IOException {
        CommandParser.getInstance().run("ls");
        String response = Jarvis.getInstance().createStudyGuide();
        Assertions.assertFalse(response.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void createInteractiveQuiz () throws IOException {
        CommandParser.getInstance().run("ls");
        String response = String.valueOf(Jarvis.getInstance().createInteractiveQuiz(SHORT_ANSWER, 3));
        Assertions.assertFalse(response.isEmpty());
    }
}
