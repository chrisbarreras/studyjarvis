import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {

//        try {
//            Class.forName("com.google.api.gax.core.CredentialsProvider");
//            System.out.println("Gax library is available.");
//        } catch (ClassNotFoundException e) {
//            System.out.println("Gax library is NOT available.");
//        }

//        Gemini g1 = new Gemini("studyjarvis", "us-west1");
        Gemini g1 = new Gemini("geminiexample-423800", "gemini-pro-vision","us-west1");
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Enter a prompt: ");
//        String prompt = scanner.nextLine();
//
//        System.out.println(g1.textInput(prompt));

//        System.out.println(Arrays.toString(ImageHandler.readImageFile("https://storage.cloud.google.com/tombarreras-studyjarvis/cmmi.png")));

        System.out.println(g1.multiModalInput(
                new String[]{"https://storage.cloud.google.com/tombarreras-studyjarvis/cmmi.png", "https://storage.cloud.google.com/tombarreras-studyjarvis/five-maturity-levels.png"},
                new String[]{"Best disney characters of all time: Mickey Mouse\n" +
                        "Woody\n" +
                        "Belle\n" +
                        "Genie\n" +
                        "Cinderella\n" +
                        "Pocahontas\n" +
                        "Captain Jack Sparrow\n" +
                        "WALL-E"},
                "Create a multiple-choice quiz for this image"));
    }
}