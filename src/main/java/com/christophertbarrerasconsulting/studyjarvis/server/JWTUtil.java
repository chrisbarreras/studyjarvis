package com.christophertbarrerasconsulting.studyjarvis.server;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.sql.*;

class JwtUtil {
    private static final String SECRET_KEY = System.getenv("STUDYJARVIS_SERVER_SECRET_KEY"); // Get the secret key from environment variables
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY);
    private static final String ISSUER = "StudyJarvisServer";
    private static final long EXPIRY = 86400000; // 24 hours in milliseconds

    public static String generateToken(String username) {
        return JWT.create()
                .withIssuer(ISSUER)
                .withClaim("username", username)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRY))
                .sign(ALGORITHM);
    }

    public static String validateToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(ALGORITHM)
                    .withIssuer(ISSUER)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim("username").asString();
        } catch (JWTVerificationException e) {
            return null; // Token validation failed
        }
    }
}
