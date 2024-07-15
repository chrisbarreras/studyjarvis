import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

import com.google.cloud.storage.transfermanager.ParallelUploadConfig;
import com.google.cloud.storage.transfermanager.TransferManager;
import com.google.cloud.storage.transfermanager.TransferManagerConfig;
import com.google.cloud.storage.transfermanager.UploadResult;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class GoogleCloudStorageHandler {

    public static void uploadDirectoryContents(String bucketName, Path sourceDirectory)
            throws IOException {
        TransferManager transferManager = TransferManagerConfig.newBuilder().build().getService();
        ParallelUploadConfig parallelUploadConfig =
                ParallelUploadConfig.newBuilder().setBucketName(bucketName).build();

        // Create a list to store the file paths
        List<Path> filePaths = new ArrayList<>();
        // Get all files in the directory
        // try-with-resource to ensure pathStream is closed
        try (Stream<Path> pathStream = Files.walk(sourceDirectory)) {
            pathStream.filter(Files::isRegularFile).forEach(filePaths::add);
        }
        List<UploadResult> results =
                transferManager.uploadFiles(filePaths, parallelUploadConfig).getUploadResults();
        for (UploadResult result : results) {
            System.out.println(
                    "Upload for "
                            + result.getInput().getName()
                            + " completed with status "
                            + result.getStatus());
        }


    }

//    public static void uploadObject(String projectId, String bucketName, String objectName, String filePath) {
//        // Initialize the client
//        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
//
//        try {
//            // Read file content
//            Path path = Paths.get(filePath);
//            byte[] data = Files.readAllBytes(path);
//
//            // Create a BlobId with bucket name, file name (object name)
//            BlobId blobId = BlobId.of(bucketName, objectName);
//
//            // Create a BlobInfo with the BlobId and content type
//            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
//
//            // Upload the file
//            storage.create(blobInfo, data);
//            System.out.println("File " + filePath + " uploaded to bucket " + bucketName + " as " + objectName);
//        } catch (IOException e) {
//            System.err.println("Failed to upload file: " + e.getMessage());
//        }
    }