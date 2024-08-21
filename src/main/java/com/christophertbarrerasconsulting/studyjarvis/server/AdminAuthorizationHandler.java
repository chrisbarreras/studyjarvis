package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.user.User;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class AdminAuthorizationHandler implements Handler {
    public static Handler getInstance() {
        return HandlerDecorator.getInstance(new AdminAuthorizationHandler());
    }

    @Override
    public void handle(@NotNull Context context) {
        try {
            String authHeader = context.header("Authorization");
            String token = authHeader.substring(7);
            String username = JwtUtil.validateToken(token);
            User user = UserReader.getUser(username);

            if (!user.getIsAdministrator()){
                context.status(403).result("Forbidden");
            }
        } catch (Exception e) {
            e.printStackTrace();
            context.status(500).result("Error");
        }
    }
}
