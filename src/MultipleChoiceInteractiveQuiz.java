import java.io.IOException;

public class MultipleChoiceInteractiveQuiz extends InteractiveQuiz {
    public MultipleChoiceInteractiveQuiz(Gemini gemini, int numberOfQuestions) throws IOException {
        super(gemini, numberOfQuestions);
    }
    @Override
    protected String getQuizPrompt(){
        return "Generate {numberOfQuestions} multiple choice questions with all of the answers at the bottom.".replace("{numberOfQuestions}", String.valueOf(numberOfQuestions));
    }

    @Override
    protected String getNextQuestionPrompt(){
        return "Here is a multiple choice quiz.\n\n" + quizText + "\n\nWhat is question number: " + questionNumber + ". Without the answer.";
    }

    @Override
    protected String evaluateAnswerPrompt(String answer){
        return "Here is a multiple choice quiz.\n\n" + quizText + "\n\nIs the answer to question: " + currentQuestion + "\nThis? \"" + answer + "\"";
    }
}
