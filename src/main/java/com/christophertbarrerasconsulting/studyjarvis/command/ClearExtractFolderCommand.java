package com.christophertbarrerasconsulting.studyjarvis.command;

import com.christophertbarrerasconsulting.studyjarvis.file.FileHandler;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class ClearExtractFolderCommand extends Command {
    ClearExtractFolderCommand () {
        commandText = "clear-extract-folder";
        shortCut = "cf";
        helpText = "Delete all files in extract folder.";
    }

    @Override
    public void run(List<String> args) throws IOException {
        if (!validPreconditions()) return;
        FileHandler.clearDirectory(Path.of(CommandSession.extractFolder));
    }

    private static boolean validPreconditions() {
        if (Objects.equals(CommandSession.extractFolder, "")) {
            System.out.println("No extract folder.");
            return false;
        }
        return true;
    }
}
