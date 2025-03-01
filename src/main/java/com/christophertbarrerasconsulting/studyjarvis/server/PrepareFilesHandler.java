package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.GoogleBucket;
import com.christophertbarrerasconsulting.studyjarvis.file.AppSettings;
import com.christophertbarrerasconsulting.studyjarvis.file.FileHandler;
import com.christophertbarrerasconsulting.studyjarvis.user.Session;
import com.christophertbarrerasconsulting.studyjarvis.user.User;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.openapi.*;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public class PrepareFilesHandler implements Handler {
    public static Handler getInstance() {
        return HandlerDecorator.getInstance(new PrepareFilesHandler());
    }

    @OpenApi(
            summary = "Prepare Files",
            description = "Prepare one or more files.",
            operationId = "prepareFiles",
            path = "/secure/files/prepare",
            methods = {HttpMethod.POST},
            responses = {
                    @OpenApiResponse(status = "200", description = "Files prepared successfully"),
                    @OpenApiResponse(status = "401", description = "Unauthorized"),
                    @OpenApiResponse(status = "500", description = "Internal server error")
            }
    )

    @Override
    public void handle(@NotNull Context context) throws Exception {
        String username = context.attribute("username");
        User user = UserReader.getUser(username);
        int userId = user.getUserId();
        GoogleBucket googleBucket = GoogleBucket.getInstance(AppSettings.BucketName.getBucketName(), userId);
        if(googleBucket.countBucket() > 0) googleBucket.clearBucket();

        Session session = SessionReader.getSession(userId);
        String uploadedFilesPath = session.getUploadedFilesPath();
        Path extractedFilesPath = Path.of(session.getExtractFolder());

        //if (FileHandler.directoryExists(extractedFilesPath)) FileHandler.clearDirectory(extractedFilesPath);

        FileHandler.extractFilesInDirectory(Path.of(uploadedFilesPath), extractedFilesPath);

        googleBucket.uploadDirectoryContents(extractedFilesPath);
    }
}
