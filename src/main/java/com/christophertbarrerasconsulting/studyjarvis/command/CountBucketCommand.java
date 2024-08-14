package com.christophertbarrerasconsulting.studyjarvis.command;

import com.christophertbarrerasconsulting.studyjarvis.GoogleBucket;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class CountBucketCommand extends Command {
    CountBucketCommand () {
        commandText = "count-bucket";
        shortCut = "kb";
        helpText = "Counts the number of items in the Google Bucket.";
    }

    @Override
    public void run(List<String> args) throws IOException {
        if (!validPreconditions()) return;
        System.out.println(GoogleBucket.getInstance(CommandSession.bucketName).countBucket());
    }
    private static boolean validPreconditions() {
        if (Objects.equals(CommandSession.bucketName, "")) {
            System.out.println("No bucket name given.");
            return false;
        }
        return true;
    }
}
