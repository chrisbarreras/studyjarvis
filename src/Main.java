import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Gemini g1 = new Gemini("studyjarvis", "us-west1");

        Scanner scanner = new Scanner(System.in);
    System.out.print("Enter a prompt: ");
    String prompt = scanner.nextLine();

    System.out.println(g1.textInput(prompt));
    }
}