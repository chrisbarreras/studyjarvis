package com.christophertbarrerasconsulting.studyjarvis.quiz;

import com.christophertbarrerasconsulting.studyjarvis.Gemini;

import java.io.IOException;

public class InteractiveQuiz {
    Gemini gemini;
    public String quizText = "";
    String currentQuestion = "";
    int questionNumber;
    public int numberOfQuestions;

    public static InteractiveQuiz getQuiz(InteractiveQuizType quizType, Gemini gemini, int numberOfQuestions) throws IOException {
        if (quizType == InteractiveQuizType.MULTIPLE_CHOICE){
            return new MultipleChoiceInteractiveQuiz(gemini, numberOfQuestions);
        }
        if (quizType == InteractiveQuizType.SHORT_ANSWER){
            return new ShortAnswerInteractiveQuiz(gemini, numberOfQuestions);
        }
        return new InteractiveQuiz(gemini, numberOfQuestions);
    }

    public InteractiveQuiz(Gemini gemini, int numberOfQuestions) throws IOException {
        this.numberOfQuestions = numberOfQuestions;
        this.gemini = gemini;
        quizText = gemini.respond(getQuizPrompt(numberOfQuestions));
    }

    protected String getQuizPrompt(int numberOfQuestions){
        return ("Generate exactly {numberOfQuestions} questions, numbered 1 through {numberOfQuestions}. " +
                "Then on a new line output the markdown heading '## Answers:' followed by the numbered answers, " +
                "one per line, matching the question numbers.")
                .replace("{numberOfQuestions}", String.valueOf(numberOfQuestions));
    }

    protected String getNextQuestionPrompt(){
        return "Here is a quiz.\n\n" + quizText + "\n\nWhat is question number: " + questionNumber + ". Without the answer.";
    }

    protected String evaluateAnswerPrompt(String answer){
        return "Here is a quiz.\n\n" + quizText + "\n\nIs the answer to question: " + currentQuestion + "\nThis? \"" + answer + "\"";
    }

    public String getNextQuestion() throws IOException {
        questionNumber++;
        currentQuestion = gemini.textInput(getNextQuestionPrompt());
        return currentQuestion;
    }

    public String evaluateAnswer(String answer) throws IOException {
        return gemini.textInput(evaluateAnswerPrompt(answer));
    }
}
