package com.christophertbarrerasconsulting.studyjarvis.command;

import com.christophertbarrerasconsulting.studyjarvis.GoogleBucket;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class UploadBucketCommand extends Command {
    UploadBucketCommand () {
        commandText = "upload-bucket";
        shortCut = "ub";
        helpText = "Upload contents of extract folder to Google Bucket.";
    }

    @Override
    public void run(List<String> args) throws IOException {
        if (!validPreconditions()) return;
        GoogleBucket.getInstance(CommandSession.bucketName).uploadDirectoryContents(Path.of(CommandSession.extractFolder));
    }

    private static boolean validPreconditions() {
        if (Objects.equals(CommandSession.extractFolder, "")) {
            System.out.println("Extract folder name is empty.");
            return false;
        }
        return true;
    }
}
