package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.file.FileHandler;
import com.christophertbarrerasconsulting.studyjarvis.user.Session;
import com.christophertbarrerasconsulting.studyjarvis.user.User;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.openapi.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jetbrains.annotations.PropertyKey;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UploadFilesHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(UploadFilesHandler.class);
    public static Handler getInstance() {
        return HandlerDecorator.getInstance(new UploadFilesHandler());
    }

    @OpenApi(
            summary = "Upload Files",
            description = "Uploads one or more files.",
            operationId = "uploadFiles",
            path = "/secure/files",
            methods = {HttpMethod.POST},
            requestBody = @OpenApiRequestBody(
                    content = {
                            @OpenApiContent(mimeType = ContentType.FORM_DATA_MULTIPART)
                    },
                    required = true
            ),
            responses = {
                    @OpenApiResponse(status = "201", description = "Files uploaded successfully"),
                    @OpenApiResponse(status = "401", description = "Unauthorized"),
                    @OpenApiResponse(status = "500", description = "Internal server error")
            }
    )

    @Override
    public void handle(@NotNull Context context) throws Exception {
        String username = context.attribute("username");
        User user = UserReader.getUser(username);
        Session session = SessionReader.getSession(user.getUserId());

        logger.info("Uploading Files Count: " + context.uploadedFiles("files").size());
        context.uploadedFiles("files").forEach(file -> {
            Path uploadedFilesPath = Path.of(session.getUploadedFilesPath());
            Path uploadedFileNamePath = Path.of(file.filename());
            Path uploadToFilePath = uploadedFilesPath.resolve(uploadedFileNamePath.getFileName());
            try {
                FileHandler.deletePathIfExists(uploadToFilePath);
                logger.info("Uploading " + file.filename() + " to " + uploadedFilesPath);
                Files.copy(file.content(), uploadToFilePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        context.status(201).result("Files uploaded successfully");
    }
}
