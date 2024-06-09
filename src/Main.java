import java.io.IOException;
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
        System.out.println(g1.imageInput("gs://tombarreras-studyjarvis/20151225_120320.jpg",
                "image/jpeg", "What year is this ornament for?"));

        System.out.println(g1.imageInput("gs://tombarreras-studyjarvis/IMG_20180824_215857835_BURST001.jpg",
                "image/jpeg", "What is in this image?"));
    }
}