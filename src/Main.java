import org.apache.poi.openxml4j.util.ZipSecureFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        ZipSecureFile.setMinInflateRatio(0.002);
//        Gemini g1 = new Gemini("studyjarvis", "us-west1");
        Gemini g1 = new Gemini("geminiexample-423800", "gemini-pro-vision","us-west1");
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Enter a prompt: ");
//        String prompt = scanner.nextLine();
//
//        System.out.println(g1.textInput(prompt));

//        System.out.println(Arrays.toString(ImageHandler.readImageFile("https://storage.cloud.google.com/tombarreras-studyjarvis/cmmi.png")));

        // Multi-modal input example
//        System.out.println(g1.multiModalInput(
//                new String[]{"https://storage.cloud.google.com/tombarreras-studyjarvis/cmmi.png", "https://storage.cloud.google.com/tombarreras-studyjarvis/five-maturity-levels.png"},
//                new String[]{"Best disney characters of all time: Mickey Mouse\n" +
//                        "Woody\n" +
//                        "Belle\n" +
//                        "Genie\n" +
//                        "Cinderella\n" +
//                        "Pocahontas\n" +
//                        "Captain Jack Sparrow\n" +
//                        "WALL-E"},
//                "Create a multiple-choice quiz for this image"));
//
//
//        PowerPointHandler.convertSlidesToImages("C:\\Users\\tomba\\OneDrive\\Documents\\FUS SFE Intro_lecture_No_10 Software Process.pptx", "D:\\slides");
//        System.out.println(PowerPointHandler.extractTextFromSlides("C:\\Users\\tomba\\OneDrive\\Documents\\FUS SFE Intro_lecture_No_10 Software Process.pptx"));

        PowerPointHandler.convertSlidesToImages("C:\\Users\\chris\\Downloads\\FUS SFE 204 SW Arch L12 - Technical Debt and Ethics.pptx", "C:\\Users\\chris\\OneDrive\\Documents\\slides");
        System.out.println(PowerPointHandler.extractTextFromSlides("C:\\Users\\chris\\Downloads\\FUS SFE 204 SW Arch L12 - Technical Debt and Ethics.pptx"));
//        PowerPointHandler.Test();
    }
}