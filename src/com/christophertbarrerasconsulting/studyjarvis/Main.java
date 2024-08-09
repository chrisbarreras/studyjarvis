package com.christophertbarrerasconsulting.studyjarvis;

import org.apache.poi.openxml4j.util.ZipSecureFile;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        // TODO:

//        Remove Console stuff from com.christophertbarrerasconsulting.studyjarvis.Jarvis
//        Add package declarations to classes

//        Create a Command Processor
//        Unit Tests
//        Functional Tests
//        Web UI
//        Multi User
//        Android App

        String slidesDir = "C:\\slides";

        ZipSecureFile.setMinInflateRatio(0.002);

//        com.christophertbarrerasconsulting.studyjarvis.file.FileHandler.clearDirectory(Path.of(slidesDir));
//        com.christophertbarrerasconsulting.studyjarvis.extraction.PDFExtractor.extract("C:\\Users\\chris\\Downloads\\sample-local-pdf.pdf", slidesDir);
//        com.christophertbarrerasconsulting.studyjarvis.extraction.PDFExtractor.extract("C:\\Users\\tomba\\Downloads\\AISlidesforINtrotoSFE28Nov2023.pdf", slidesDir);
//        com.christophertbarrerasconsulting.studyjarvis.extraction.PowerPointExtractor.extract("C:\\Users\\chris\\Downloads\\Arch Quiz 2 All Slides.pptx", slidesDir);
//
//        com.christophertbarrerasconsulting.studyjarvis.GoogleBucket googleBucket = new com.christophertbarrerasconsulting.studyjarvis.GoogleBucket("chris_barreras_studyjarvis");
//        googleBucket.clearBucket();
//        googleBucket.uploadDirectoryContents(Path.of(slidesDir));

        Jarvis jarvis = new Jarvis("chris_barreras_studyjarvis","studyjarvis", "gemini-1.5-pro", "us-west1");
//        com.christophertbarrerasconsulting.studyjarvis.Jarvis jarvis = new com.christophertbarrerasconsulting.studyjarvis.Jarvis("tombarreras-studyjarvis","geminiexample-423800", "gemini-1.5-pro", "us-west1");
//        System.out.println(jarvis.createQuiz(10));
//        System.out.println(jarvis.createKeyPoints());
//        System.out.println(jarvis.createStudyGuide());
//        jarvis.createEndlessMultiQuestions();
//        jarvis.createInteractiveQuiz(com.christophertbarrerasconsulting.studyjarvis.quiz.InteractiveQuizType.SHORT_ANSWER);
        System.out.print(jarvis.createComprehensiveNotes());
//        System.out.println(jarvis.askQuestion());

//        FileWriter fileWriter = new FileWriter("comprehensiveNotes.txt", true);
//
//        // Create a PrintWriter from FileWriter
//        PrintWriter printWriter = new PrintWriter(fileWriter);
//
//        // Write text to file
//        printWriter.println(jarvis.createComprehensiveNotes());
//
//        // Close PrintWriter to flush the output to the file and close the file
//        printWriter.close();


//        com.christophertbarrerasconsulting.studyjarvis.Gemini g1 = new com.christophertbarrerasconsulting.studyjarvis.Gemini("studyjarvis", "gemini-pro-vision", "us-west1");
//        com.christophertbarrerasconsulting.studyjarvis.Gemini g1 = new com.christophertbarrerasconsulting.studyjarvis.Gemini("geminiexample-423800", "gemini-pro-vision","us-west1");
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Enter a prompt: ");
//        String prompt = scanner.nextLine();
//
//        System.out.println(g1.textInput(prompt));

//        System.out.println(Arrays.toString(com.christophertbarrerasconsulting.studyjarvis.file.ImageHandler.readImageFile("https://storage.cloud.google.com/tombarreras-studyjarvis/cmmi.png")));

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