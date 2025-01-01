package com.christophertbarrerasconsulting.studyjarvis;

import com.google.cloud.storage.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Stream;

import com.google.cloud.storage.Storage.BlobListOption;
import com.google.cloud.storage.Storage.BlobField;

public class GoogleBucket {
    private static final int DEFAULT_USER_ID = -1;
    String bucketName;
    private final Storage storage;
    public final String prefix;

    public static GoogleBucket getInstance(String bucketName) {
        return getInstance(bucketName, DEFAULT_USER_ID);
    }

    public static GoogleBucket getInstance(String bucketName, int userId) {
        if (Objects.equals(bucketName, "")) {
            throw new IllegalArgumentException("Bucket name is empty.");
        }
        return new GoogleBucket(bucketName, userId);
    }

    private GoogleBucket (String bucketName, int userId){
        this.bucketName = bucketName;
        this.storage = StorageOptions.getDefaultInstance().getService();
        this.prefix = "user " + userId + ":";
    }

    public void uploadDirectoryContents(Path sourceDirectory) throws IOException {
        try (Stream<Path> pathStream = Files.walk(sourceDirectory)) {
            pathStream
                    // Only process regular files (skip directories, symlinks, etc.)
                    .filter(Files::isRegularFile)
                    .forEach(filePath -> {
                        // Compute relative path (so subdirectories are preserved in the object name)
                        Path relativePath = sourceDirectory.relativize(filePath);
                        // Ensure forward slashes in the object name
                        String normalizedPath = relativePath.toString().replace("\\", "/");
                        // Prepend the "folder" prefix
                        String objectName = prefix + normalizedPath;

                        // Create a BlobId, then a BlobInfo describing the file to upload
                        BlobId blobId = BlobId.of(bucketName, objectName);
                        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

                        // Read file bytes and upload
                        try {
                            byte[] fileBytes = Files.readAllBytes(filePath);
                            storage.create(blobInfo, fileBytes);
                            System.out.printf("Uploaded %s to gs://%s/%s%n", filePath, bucketName, objectName);
                        } catch (IOException e) {
                            System.err.printf("Failed to upload %s: %s%n", filePath, e.getMessage());
                        }
                    });
        }
    }


    public void clearBucket(){
        // Create a Storage client
        Storage storage = StorageOptions.getDefaultInstance().getService();

        // List and delete all objects in the bucket
        Bucket bucket = storage.get(bucketName);
        if (bucket == null) {
            System.out.println("Bucket not found");
            return;
        }

        // Iterate over the objects in the bucket and delete each one
        for (Blob blob : storage.list(bucketName, BlobListOption.fields(BlobField.NAME)).iterateAll()) {
            String blobName = blob.getName();
            if (blobName.startsWith(prefix)) {
                System.out.println("Deleting object: " + blobName);
                boolean deleted = storage.delete(bucketName, blobName);
                if (deleted) {
                    System.out.println("Deleted: " + blobName);
                } else {
                    System.out.println("Failed to delete: " + blobName);
                }
            }
            else {
                System.out.println("Skipping: " + blobName);
            }
        }

        System.out.println("Bucket cleared.");
    }

    public ArrayList<String> getURIs(){
        // Create a Storage client
        Storage storage = StorageOptions.getDefaultInstance().getService();

        // List URIs of all objects in the bucket
        ArrayList<String> uris = new ArrayList<>();
        for (Blob blob : storage.list(bucketName, BlobListOption.fields(Storage.BlobField.NAME)).iterateAll()) {
            if (blob.getName().startsWith(prefix)) {
                String uri = "gs://" + bucketName + "/" + blob.getName();
                uris.add(uri);
            }
        }

       return uris;
    }

    public int countBucket () {
        // Instantiate a Google Cloud Storage client
        Storage storage = StorageOptions.getDefaultInstance().getService();

        // Get the bucket
        Bucket bucket = storage.get(bucketName);

        // Count the number of blobs (objects) in the bucket
        int objectCount = 0;

        for (Blob blob : bucket.list().iterateAll()) {
            if(blob.getName().startsWith(prefix)) objectCount++;
        }
        return objectCount;
    }
}