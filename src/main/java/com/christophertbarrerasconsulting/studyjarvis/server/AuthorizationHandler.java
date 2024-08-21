package com.christophertbarrerasconsulting.studyjarvis.server;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class AuthorizationHandler implements Handler {
    public static AuthorizationHandler getInstance() {
        return new AuthorizationHandler();
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        String authHeader = context.header("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username = JwtUtil.validateToken(token);
            if (username == null) {
                context.status(401).result("Unauthorized");
            } else {
                context.attribute("username", username); // Pass username to downstream handlers
            }
        } else {
            context.status(401).result("Unauthorized");
        }
    }
}
