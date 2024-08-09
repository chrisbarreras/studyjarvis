package com.christophertbarrerasconsulting.studyjarvis.quiz;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class ConsoleInteractiveQuizRunner {
    public void runQuiz(InteractiveQuiz interactiveQuiz) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String answer = "Z";
        System.out.println("Enter 0 to quit.\n");

        while (true) {
            System.out.println("Generating next " + interactiveQuiz.numberOfQuestions + " questions...\n");
            int currentQuestion = 0;

            for (int index = 1; index <= interactiveQuiz.numberOfQuestions; index++) {
                currentQuestion++;
                String question = interactiveQuiz.getNextQuestion();
                System.out.println(currentQuestion + ") " + question);
                System.out.print("Enter: ");
                answer = scanner.nextLine();

                if (Objects.equals(answer, "0")){
                    break;
                }

                System.out.println(interactiveQuiz.evaluateAnswer(answer));
            }

            if (Objects.equals(answer, "0")){
                System.out.println("\nQuitting...");
                scanner.close();
                break;
            }
        }
    }
}
