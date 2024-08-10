package com.christophertbarrerasconsulting.studyjarvis.command;

import com.christophertbarrerasconsulting.studyjarvis.extraction.PDFExtractor;
import com.christophertbarrerasconsulting.studyjarvis.extraction.PowerPointExtractor;
import com.christophertbarrerasconsulting.studyjarvis.file.FileHandler;

import java.io.IOException;
import java.util.Objects;

public class ExtractFileCommand extends Command {
    @Override
    public void run() throws IOException {
        if (Objects.equals(CommandParser.secondPartOfList, "")) {
            throw new IllegalArgumentException("There is no output directory given.");
        }
        String fileType = FileHandler.getFileType(CommandParser.secondPartOfList);

        if (fileType.equals("pdf")){
            PDFExtractor.extract(CommandParser.secondPartOfList, CommandSession.extractFolder);
        }
        else {
            PowerPointExtractor.extract(CommandParser.secondPartOfList, CommandSession.extractFolder);
        }
    }
}
