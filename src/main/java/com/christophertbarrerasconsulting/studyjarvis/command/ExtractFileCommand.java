package com.christophertbarrerasconsulting.studyjarvis.command;

import com.christophertbarrerasconsulting.studyjarvis.extraction.PDFExtractor;
import com.christophertbarrerasconsulting.studyjarvis.extraction.PowerPointExtractor;
import com.christophertbarrerasconsulting.studyjarvis.file.FileHandler;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ExtractFileCommand extends Command {
    ExtractFileCommand () {
        commandText = "extract-file";
        shortCut = "ef";
        helpText = "Extract the contents of a file into the extract folder.";
    }

    @Override
    public void run(List<String> args) throws IOException {
        String filePath = getFilePath(args);
        if (filePath == null) return;
        String fileType = FileHandler.getFileType(filePath);

        if (fileType.equalsIgnoreCase("pdf")) {
            PDFExtractor.extract(filePath, CommandSession.extractFolder);
        } else {
            PowerPointExtractor.extract(filePath, CommandSession.extractFolder);
        }
    }

    private static String getFilePath(List<String> args) {
        if (args.isEmpty()) {
            System.out.println("No file path given.");
            return null;
        }
        String filePath = args.get(0);
        File file = new File(filePath);

        if (file.exists() && file.isFile()) {
            String fileType = FileHandler.getFileType(filePath);
            if ("pdf".equalsIgnoreCase(fileType) || "ppt".equalsIgnoreCase(fileType) || "pptx".equalsIgnoreCase(fileType)) {
                return filePath;
            } else {
                System.out.println("The file is not a PDF or PowerPoint.");
                return null;
            }
        } else {
            System.out.println("The file does not exist.");
            return null;
        }
    }
}
