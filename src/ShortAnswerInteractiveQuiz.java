import java.io.IOException;

public class ShortAnswerInteractiveQuiz extends InteractiveQuiz{
    public ShortAnswerInteractiveQuiz(Gemini gemini, int numberOfQuestions) throws IOException {
        super(gemini, numberOfQuestions);
    }

    @Override
    protected String getQuizPrompt(int numberOfQuestions){
        return "Generate {numberOfQuestions} short answer questions with all of the answers at the bottom.".replace("{numberOfQuestions}", String.valueOf(numberOfQuestions));
    }

    @Override
    protected String getNextQuestionPrompt(){
        return "Here is a short answer quiz.\n\n" + quizText + "\n\nWhat is question number: " + questionNumber + ". Without the answer.";
    }

    @Override
    protected String evaluateAnswerPrompt(String answer){
        return "Here is a short answer quiz.\n\n" + quizText + "\n\nIs the answer to question: " + currentQuestion + "\nThis? \"" + answer + "\"";
    }
}
