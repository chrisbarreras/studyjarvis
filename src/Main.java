import com.google.storage.v2.Bucket;
import io.opencensus.metrics.export.Distribution;
import org.apache.poi.openxml4j.util.ZipSecureFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) throws IOException {
        // TODO:

        // string Jarvis::initialize(googleCloudBucket)
        // string Jarvis::createQuiz(numberOfQuestions)
        // string Jarvis::generateKeyPoints(numberOfKeyPoints)
        // string Jarvis::generateStudyGuide(numberOfKeyPoints)

        // PdfHandler::extract(pdfPath, outputFolderPath)
        //  <number> <pdf name>.png
        //  <number> <pdf name>.txt

        // GoogleSlidesHandler::extract(Uri googleSlidesUrl, String outputFolderPath)

        // Add some unit tests

        // Interactive
        // string Jarvis::ask(question)
        // void Jarvis::generateQuizQuestion(bool multipleChoice)
        // bool Jarvis::checkAnswer(question, answer)

        String slidesDir = "C:\\slides";

        ZipSecureFile.setMinInflateRatio(0.002);

//        FileHandler.clearDirectory(Path.of(slidesDir));
//        PowerPointHandler.extract("C:\\Users\\chris\\Downloads\\Arch Quiz 2 All Slides.pptx", slidesDir);
//
//        GoogleBucket googleBucket = new GoogleBucket("chris_barreras_studyjarvis");
//        googleBucket.clearBucket();
//        googleBucket.uploadDirectoryContents(Path.of(slidesDir));

        Jarvis jarvis = new Jarvis("chris_barreras_studyjarvis","studyjarvis", "gemini-1.5-pro", "us-west1");
//        System.out.println(jarvis.createQuiz(10));
//        System.out.println(jarvis.createKeyPoints());
//        System.out.println(jarvis.createStudyGuide());
//        jarvis.createEndlessMultiQuestions();
        jarvis.createInteractiveQuiz(InteractiveQuizType.SHORT_ANSWER);


//        Gemini g1 = new Gemini("studyjarvis", "gemini-pro-vision", "us-west1");
//        Gemini g1 = new Gemini("geminiexample-423800", "gemini-pro-vision","us-west1");
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Enter a prompt: ");
//        String prompt = scanner.nextLine();
//
//        System.out.println(g1.textInput(prompt));

//        System.out.println(Arrays.toString(ImageHandler.readImageFile("https://storage.cloud.google.com/tombarreras-studyjarvis/cmmi.png")));

        // Multi-modal input example
//        g1.initializeMultiModalInput(
//                new String[]{"gs://chris_barreras_studyjarvis/1 1 Arch Quiz 2 All Slides.png", "gs://chris_barreras_studyjarvis/1 2 Arch Quiz 2 All Slides.png", "gs://chris_barreras_studyjarvis/Fred loves UML.txt"}, );

//        System.out.println(g1.respond("Create a quiz from this content"));
//        System.out.print(g1.respond("Who loves UML?"));
//
//        PowerPointHandler.extract("C:\\Users\\tomba\\OneDrive\\Documents\\FUS SFE Intro_lecture_No_10 Software Process.pptx", slidesDir);
//        PowerPointHandler.extract("C:\\Users\\chris\\Downloads\\Arch Quiz 2 All Slides.pptx", slidesDir);


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