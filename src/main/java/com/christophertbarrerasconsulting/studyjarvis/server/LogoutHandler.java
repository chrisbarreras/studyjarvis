package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LogoutHandler implements Handler {

    static Handler getInstance(Handler deleteSessionHandler){
        return new LogoutHandler();
    }

    public static Handler getInstance() {
        return getInstance(DeleteSessionHandler.getInstance());
    }

    @Override
    public void handle(@org.jetbrains.annotations.NotNull Context context) throws Exception {
        try  {
            SessionWriter.deleteSessions(AuthorizationHandler.getUsernameFromContext(context));
        } catch (SQLException e) {
            e.printStackTrace();
            context.status(500).result("Error");
        }
    }
}
