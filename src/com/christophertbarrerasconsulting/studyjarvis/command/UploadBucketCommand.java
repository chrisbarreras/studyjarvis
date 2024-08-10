package com.christophertbarrerasconsulting.studyjarvis.command;

import com.christophertbarrerasconsulting.studyjarvis.GoogleBucket;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public class UploadBucketCommand extends Command {
    @Override
    public void run() throws IOException {
        if (Objects.equals(CommandSession.bucketName, "")) {
            throw new IllegalArgumentException("Bucket name is empty.");
        }
        if (Objects.equals(CommandSession.extractFolder, "")) {
            throw new IllegalArgumentException("Extract folder name is empty.");
        }

        GoogleBucket googleBucket = new GoogleBucket(CommandSession.bucketName);
        googleBucket.uploadDirectoryContents(Path.of(CommandSession.extractFolder));
    }
}
