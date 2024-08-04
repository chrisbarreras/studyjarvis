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
        return gemini.respond("Create a quiz with this many questions and put answers at the bottom: " + numberOfQuestions);
    }

    public String createKeyPoints() throws IOException {
        return gemini.respond("Generate key points on all of the topics.");
    }

    public String createStudyGuide() throws IOException {
        return gemini.respond("Generate a study guide including all of the topics.");
    }

    public void createEndlessMultiQuestions() throws IOException {
        Scanner scanner = new Scanner(System.in);
        String input = "Z";
        System.out.println("Enter 0 to quit.\n");
        String prompt;

        while (true) {
            System.out.println("Generating next ten questions...\n");
            prompt = gemini.respond("Generate ten multiple choice questions with all of the answers at the bottom.");

            for (int index = 1; index <= 10; index++) {
                System.out.println(index + ") " + gemini.textInput("Here is a quiz.\n\n" + prompt + "\n\nWhat is question number: " + index + ". Without the answer."));
                System.out.print("Enter: ");
                input = scanner.nextLine();

                if (Objects.equals(input, "0")){
                    break;
                }

                System.out.println(gemini.textInput("Here is a quiz.\n\n" + prompt + "\n\nIs the answer to question: " + index + "\nThis: " + input + "?"));
            }

            if (Objects.equals(input, "0")){
                System.out.println("\nQuitting...");
                scanner.close();
                break;
            }
        }
    }

    public void createEndlessShortQuestions() throws IOException {
        Scanner scanner = new Scanner(System.in);
        String input = "Z";
        System.out.println("Enter 0 to quit.\n");
        String prompt;

        while (true) {
            System.out.println("Generating next ten questions...\n");
            prompt = gemini.respond("Generate ten short answer questions with all of the answers at the bottom.");

            for (int index = 1; index <= 10; index++) {
                System.out.println(index + ") " + gemini.textInput("Here is a short answer quiz.\n\n" + prompt + "\n\nWhat is question number: " + index + ". Without the answer."));
                System.out.print("Enter: ");
                input = scanner.nextLine();

                if (Objects.equals(input, "0")){
                    break;
                }

                System.out.println(gemini.textInput("Here is a short answer quiz.\n\n" + prompt + "\n\nIs the answer to question: " + index + "\nThis? \"" + input + "\""));
            }

            if (Objects.equals(input, "0")){
                System.out.println("\nQuitting...");
                scanner.close();
                break;
            }
        }
    }
}
