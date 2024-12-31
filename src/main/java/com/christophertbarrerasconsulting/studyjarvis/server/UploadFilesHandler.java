package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.user.Session;
import com.christophertbarrerasconsulting.studyjarvis.user.User;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UploadFilesHandler implements Handler {
    public static Handler getInstance() {
        return HandlerDecorator.getInstance(new UploadFilesHandler());
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        String username = context.attribute("username");
        User user = UserReader.getUser(username);
        Session session = SessionReader.getSession(user.getUserId());

        context.uploadedFiles("files").forEach(file -> {
            Path uploadedFilesPath = Path.of(session.getUploadedFilesPath());
            Path uploadedFileNamePath = Path.of(file.filename());
            Path uploadToFilePath = uploadedFilesPath.resolve(uploadedFileNamePath.getFileName()); //Append UserID to this TODO
            try {
                Files.copy(file.content(), uploadToFilePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
//            try (Connection conn = Database.connect()) {
//                PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO files (username, filename, filecontent) VALUES (?, ?)");
//                insertStmt.setString(1, username);
//                insertStmt.setString(2, file.filename());
//                insertStmt.setBlob(3, file.content());
//                insertStmt.executeUpdate();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
        });
        context.status(201).result("Files uploaded successfully");
    }
}
