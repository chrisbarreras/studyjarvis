package com.christophertbarrerasconsulting.studyjarvis.command;

import com.christophertbarrerasconsulting.studyjarvis.GoogleBucket;

import java.io.IOException;
import java.util.Objects;

public class ClearBucketCommand extends Command {
    ClearBucketCommand () {
        commandText = "clear-bucket";
        shortCut = "cb";
        helpText = "Deletes all files in the Google Bucket";
    }

    @Override
    public void run() throws IOException {
        if (Objects.equals(CommandSession.bucketName, "")) {
            throw new IllegalArgumentException("Bucket name is empty.");
        }

        GoogleBucket googleBucket = new GoogleBucket(CommandSession.bucketName);
        googleBucket.clearBucket();
    }
}
