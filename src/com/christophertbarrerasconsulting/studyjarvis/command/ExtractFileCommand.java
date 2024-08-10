package com.christophertbarrerasconsulting.studyjarvis.command;

import com.christophertbarrerasconsulting.studyjarvis.extraction.PDFExtractor;
import com.christophertbarrerasconsulting.studyjarvis.extraction.PowerPointExtractor;
import com.christophertbarrerasconsulting.studyjarvis.file.FileHandler;

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
        if (Objects.equals(args.get(0), "")) {
            throw new IllegalArgumentException("There is no output directory given.");
        }
        String fileType = FileHandler.getFileType(args.get(0));

        if (fileType.equals("pdf")){
            PDFExtractor.extract(args.get(0), CommandSession.extractFolder);
        }
        else {
            PowerPointExtractor.extract(args.get(0), CommandSession.extractFolder);
        }
    }
}
