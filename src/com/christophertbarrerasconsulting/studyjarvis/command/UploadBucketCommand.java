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
        if (Objects.equals(CommandSession.extractFolder, "")) {
            throw new IllegalArgumentException("Extract folder name is empty.");
        }

        GoogleBucket.getInstance(CommandSession.bucketName).uploadDirectoryContents(Path.of(CommandSession.extractFolder));
    }
}
