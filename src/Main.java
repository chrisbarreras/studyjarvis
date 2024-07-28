import org.apache.poi.openxml4j.util.ZipSecureFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        // TODO:

        // PowerPointHandler::extract(powerPointFileName, outputFolderPath)
        //  <lesson number> <page number> <pptx name>.png
        //  <lesson number> <page number> <pptx name>.txt

        // GoogleCloudStorageHandler::clearBucket(bucketName)

        // string Jarvis::initialize(googleCloudBucket)
        // string Jarvis::createQuiz(numberOfQuestions)
        // string Jarvis::generateKeyPoints(numberOfKeyPoints)
        // string Jarvis::generateStudyGuide(numberOfKeyPoints)

        // PdfHandler::extract(pdfPath, outputFolderPath)
        //  <number> <pdf name>.png
        //  <number> <pdf name>.txt

        // Add some unit tests

        // Interactive
        // string Jarvis::ask(question)
        // void Jarvis::generateQuizQuestion(bool multipleChoice)
        // bool Jarvis::checkAnswer(question, answer)


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
        String slidesDir = "D:\\slides";
        PowerPointHandler.extract("C:\\Users\\tomba\\OneDrive\\Documents\\FUS SFE Intro_lecture_No_10 Software Process.pptx", slidesDir);
//        PowerPointHandler.convertSlidesToImages("C:\\Users\\tomba\\OneDrive\\Documents\\FUS SFE Intro_lecture_No_10 Software Process.pptx", slidesDir);
//        String text = PowerPointHandler.extractTextFromSlides("C:\\Users\\tomba\\OneDrive\\Documents\\FUS SFE Intro_lecture_No_10 Software Process.pptx");

//        PowerPointHandler.convertSlidesToImages("C:\\Users\\chris\\Downloads\\FUS SFE 204 SW Arch L12 - Technical Debt and Ethics.pptx", "C:\\Users\\chris\\OneDrive\\Documents\\slides");
//        System.out.println(PowerPointHandler.extractTextFromSlides("C:\\Users\\chris\\Downloads\\FUS SFE 204 SW Arch L12 - Technical Debt and Ethics.pptx"));
//        PowerPointHandler.Test();

//        GoogleCloudStorageHandler.uploadDirectoryContents("tombarreras-studyjarvis", Path.of(slidesDir));


        // Multi-modal input example
//        System.out.println(g1.multiModalInput(
//                new String[]{"https://storage.cloud.google.com/tombarreras-studyjarvis/D%3A%5Cslides%5Cslide12.png",
//                        "https://storage.cloud.google.com/tombarreras-studyjarvis/D%3A%5Cslides%5Cslide2.png",
//                        "https://storage.cloud.google.com/tombarreras-studyjarvis/D%3A%5Cslides%5Cslide3.png",
//                        "https://storage.cloud.google.com/tombarreras-studyjarvis/D%3A%5Cslides%5Cslide4.png",
//                        "https://storage.cloud.google.com/tombarreras-studyjarvis/D%3A%5Cslides%5Cslide5.png"
//                },
//                new String[]{"foo"},
//                "Create a multiple-choice quiz for the content of these images"));

    }
}