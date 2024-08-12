package com.christophertbarrerasconsulting.studyjarvis.command;

import com.christophertbarrerasconsulting.studyjarvis.extraction.PDFExtractor;
import com.christophertbarrerasconsulting.studyjarvis.extraction.PowerPointExtractor;
import com.christophertbarrerasconsulting.studyjarvis.file.FileHandler;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ExtractFileCommand extends Command {
    ExtractFileCommand () {
        commandText = "extract-file";
        shortCut = "ef";
        helpText = "Extract the contents of a file into the extract folder.";
    }

    @Override
    public void run(List<String> args) throws IOException {
        String outputDirectory = getOutputDirectory(args);
        if (outputDirectory == null) return;
        String fileType = FileHandler.getFileType(outputDirectory);

        if (fileType.equals("pdf")){
            PDFExtractor.extract(outputDirectory, CommandSession.extractFolder);
        }
        else {
            PowerPointExtractor.extract(outputDirectory, CommandSession.extractFolder);
        }
    }

    private static String getOutputDirectory(List<String> args) {
        if (args.isEmpty()) {
            System.out.println("No output directory given.");
            return null;
        }
        String outputDirectory = args.get(0);
        File folder = new File(outputDirectory);
        if (folder.exists() && folder.isDirectory()) {
            return outputDirectory;
        } else {
            System.out.println("The folder does not exist.");
            return null;
        }
    }
}
