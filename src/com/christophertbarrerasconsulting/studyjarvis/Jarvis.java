package com.christophertbarrerasconsulting.studyjarvis;

import com.christophertbarrerasconsulting.studyjarvis.quiz.InteractiveQuiz;
import com.christophertbarrerasconsulting.studyjarvis.quiz.InteractiveQuizType;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class Jarvis {
    Gemini gemini;
    GoogleBucket bucket;

    public Jarvis(String bucketName, String geminiProjectID, String geminiModelName, String geminiLocation) {
        gemini = new Gemini(geminiProjectID, geminiModelName, geminiLocation);
        bucket = new GoogleBucket(bucketName);
        gemini.initializeMultiModalInput(bucket.getURIs().toArray(new String[0]));
    }

    public String createQuiz(int numberOfQuestions) throws IOException {
        return (new InteractiveQuiz(gemini, numberOfQuestions)).quizText;
    }

    public String askQuestion() throws IOException {
        return gemini.textInput("Here are some notes:\n\n" + gemini.respond("Generate comprehensive notes on all of the topics.") + "\n\nWhen is it appropriate to use CMMI?");
    }

    public String createComprehensiveNotes() throws IOException {
        return gemini.respond("Generate detailed, comprehensive notes on all of the topics.");
    }

    public String createKeyPoints() throws IOException {
        return gemini.respond("Generate key points on all of the topics.");
    }

    public String createStudyGuide() throws IOException {
        return gemini.respond("Generate a study guide including all of the topics.");
    }

    public void createInteractiveQuiz(InteractiveQuizType quizType) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String answer = "Z";
        System.out.println("Enter 0 to quit.\n");

        while (true) {
            System.out.println("Generating next ten questions...\n");
            int numberOfQuestions = 10;
            int currentQuestion = 0;
            InteractiveQuiz quiz = InteractiveQuiz.getQuiz (quizType, gemini, numberOfQuestions);

            for (int index = 1; index <= numberOfQuestions; index++) {
                currentQuestion++;
                String question = quiz.getNextQuestion();
                System.out.println(currentQuestion + ") " + question);
                System.out.print("Enter: ");
                answer = scanner.nextLine();

                if (Objects.equals(answer, "0")){
                    break;
                }

                System.out.println(quiz.evaluateAnswer(answer));
            }

            if (Objects.equals(answer, "0")){
                System.out.println("\nQuitting...");
                scanner.close();
                break;
            }
        }
    }
}
