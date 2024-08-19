package com.christophertbarrerasconsulting.studyjarvis.server;

import org.mindrot.jbcrypt.BCrypt;

class PasswordHasher {
    static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
