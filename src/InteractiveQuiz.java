import java.io.IOException;

public class InteractiveQuiz {
    Gemini gemini;
    int numberOfQuestions = 10;
    String quizText = "";
    String currentQuestion = "";
    int questionNumber;

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
        this.gemini = gemini;
        this.numberOfQuestions = numberOfQuestions;
        quizText = gemini.respond(getQuizPrompt());
    }

    protected String getQuizPrompt(){
        return "Generate {numberOfQuestions} questions with all of the answers at the bottom.".replace("{numberOfQuestions}", String.valueOf(numberOfQuestions));
    }

    protected String getNextQuestionPrompt(){
        return "Here is a quiz.\n\n" + quizText + "\n\nWhat is question number: " + questionNumber + ". Without the answer.";
    }

    protected String evaluateAnswerPrompt(String answer){
        return "Here is a quiz.\n\n" + quizText + "\n\nIs the answer to question: " + currentQuestion + "\nThis? \"" + answer + "\"";
    }

    public String getNextQuestion() throws IOException {
        numberOfQuestions++;
        currentQuestion = gemini.textInput(getNextQuestionPrompt());
        return currentQuestion;
    }

    public String evaluateAnswer(String answer) throws IOException {
        return gemini.textInput(evaluateAnswerPrompt(answer));
    }
}
