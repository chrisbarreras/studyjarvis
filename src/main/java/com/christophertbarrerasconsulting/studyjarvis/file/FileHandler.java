package com.christophertbarrerasconsulting.studyjarvis.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;
import okhttp3.*;

public class FileHandler {

    public static String createNewTempFolder(String prefix) throws IOException {
        return Files.createTempDirectory(prefix).toString();
    }

    private static int extractNumber(String input) {
        // Split the input string based on the first space
        String[] parts = input.split(" ", 2);

        // Parse the first part as an integer
        try {
            return Integer.parseInt(parts[0]);
        } catch (NumberFormatException e) {
            System.out.println("Error: The provided input does not start with a valid number.");
            return -1;
        }
    }

    public static int getNextFileNumber(String folderPath){
        List<String> fileList = new ArrayList<>();
        File folder = new File(folderPath);

        int maxNumber = 0;

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        int currentNumber = extractNumber(file.getName());
                        if (currentNumber > maxNumber){
                            maxNumber = currentNumber;
                        }
                    }
                }
            }
        } else {
            System.out.println("The provided path is not a valid directory.");
        }

        return maxNumber + 1;
    }

    private static String extractFileNameWithoutExtension(String filePath) {
        // Create a File object from the file path
        File file = new File(filePath);

        // Get the name of the file
        String fileName = file.getName();

        // Find the position of the last period character in the file name
        int lastIndexOfDot = fileName.lastIndexOf(".");

        // If there's no dot in the file name, return the file name as is
        if (lastIndexOfDot == -1) {
            return fileName;
        }

        // Otherwise, return the substring of the file name up to (but not including) the last dot
        return fileName.substring(0, lastIndexOfDot);
    }

    public static String concatenatePath(String folderPath, String fileName) {
        // Create a File object for the folder path
        File folder = new File(folderPath);

        // Create a File object for the full path by combining the folder path and file name
        File fullPath = new File(folder, fileName);

        // Return the absolute path as a string
        return fullPath.getAbsolutePath();
    }

    public static String getNextFilePath(String outputFolderPath, String sourceFilePath, int fileNumber, int pageNumber, String fileType){
        return concatenatePath(outputFolderPath, fileNumber + " " + pageNumber + " " + extractFileNameWithoutExtension(sourceFilePath) + fileType );
    }

    public static void writeTextToFile(String text, String filePath) throws IOException {
        // Create a BufferedWriter object
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));) {
            // Write the text to the file
            writer.write(text);
        }
    }

    public static String mimeTypeFromUri(String uri){
        if (uri.toLowerCase().endsWith(".png")) {
            return"image/png";
        }
        return "text/plain";
    }

    public static boolean directoryExists(String path){
        Path folderPath = Path.of(path);

        // Check if the folder exists and is indeed a directory
        return directoryExists(folderPath);
    }

    public static boolean directoryExists(Path path){
        // Check if the folder exists and is indeed a directory
        return (Files.exists(path) && Files.isDirectory(path));
    }

    public static void clearDirectory(Path directory) throws IOException {
        try (Stream<Path> walk = Files.walk(directory)) {
            walk.sorted(Comparator.reverseOrder())
                    .filter(path -> !path.equals(directory)) // Exclude the root directory
                    .forEach(path1 -> {
                        try {
                            deletePath(path1);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }

    public static void deletePath(Path path) throws IOException {
        Files.delete(path);
    }

    public static void deletePathIfExists(Path path) throws IOException {
        if (directoryExists(path)) {
            clearDirectory(path);
            Files.delete(path);
        }
    }

    public static void deletePathIfExists(String path) throws IOException {
        deletePathIfExists(Path.of(path));
    }

    public static String getFileType(String filePath) {
        // Find the last occurrence of the dot in the file path
        int lastDotIndex = filePath.lastIndexOf('.');

        // If no dot is found or it's at the beginning, return an empty string
        if (lastDotIndex == -1 || lastDotIndex == 0) {
            return "";
        }

        // Return the file extension
        return filePath.substring(lastDotIndex + 1);
    }

    public static MediaType getMediaType(File file) {
        String fileName = file.getName().toLowerCase();

        // Common image types
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return MediaType.parse("image/jpeg");
        } else if (fileName.endsWith(".png")) {
            return MediaType.parse("image/png");
        } else if (fileName.endsWith(".gif")) {
            return MediaType.parse("image/gif");
        }

        // PDF
        if (fileName.endsWith(".pdf")) {
            return MediaType.parse("application/pdf");
        }

        // Microsoft Word
        if (fileName.endsWith(".doc")) {
            return MediaType.parse("application/msword");
        } else if (fileName.endsWith(".docx")) {
            return MediaType.parse("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        }

        // Microsoft Excel
        if (fileName.endsWith(".xls")) {
            return MediaType.parse("application/vnd.ms-excel");
        } else if (fileName.endsWith(".xlsx")) {
            return MediaType.parse("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        }

        // Microsoft PowerPoint
        if (fileName.endsWith(".ppt")) {
            return MediaType.parse("application/vnd.ms-powerpoint");
        } else if (fileName.endsWith(".pptx")) {
            return MediaType.parse("application/vnd.openxmlformats-officedocument.presentationml.presentation");
        }

        // Text
        if (fileName.endsWith(".txt")) {
            return MediaType.parse("text/plain");
        }

        // Default / fallback
        return MediaType.parse("application/octet-stream");
    }
}
