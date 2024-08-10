package com.christophertbarrerasconsulting.studyjarvis.command;

import com.christophertbarrerasconsulting.studyjarvis.file.FileHandler;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public class ClearExtractFolderCommand extends Command {
    ClearExtractFolderCommand () {
        commandText = "clear-extract-folder";
        shortCut = "cf";
        helpText = "Delete all files in extract folder.";
    }

    @Override
    public void run() throws IOException {
        if (Objects.equals(CommandSession.extractFolder, "")) {
            throw new IllegalArgumentException("Extract folder can't be empty string.");
        }
        FileHandler.clearDirectory(Path.of(CommandSession.extractFolder));
    }
}
