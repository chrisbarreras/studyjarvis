package com.christophertbarrerasconsulting.studyjarvis.command;

import com.christophertbarrerasconsulting.studyjarvis.GoogleBucket;
import com.christophertbarrerasconsulting.studyjarvis.file.AppSettings;

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
        GoogleBucket.getInstance(CommandSession.bucketName).clearBucket();
    }
}
