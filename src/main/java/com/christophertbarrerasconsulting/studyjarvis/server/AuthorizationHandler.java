package com.christophertbarrerasconsulting.studyjarvis.server;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class AuthorizationHandler implements Handler {
    public static AuthorizationHandler getInstance() {
        return new AuthorizationHandler();
    }

    public static String getUsernameFromContext(Context context){
        String authHeader = context.header("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return JwtUtil.validateToken(token);
        } else {
            return null;
        }
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        String username = getUsernameFromContext(context);

        if (username == null) {
            context.status(401).result("Unauthorized");
        } else {
            context.attribute("username", username); // Pass username to downstream handlers
        }
    }
}
