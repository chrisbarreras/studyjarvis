package com.christophertbarrerasconsulting.studyjarvis.user;

public class LoginResponse {
    private String username;
    private boolean isAdmin;

    public LoginResponse() {}

    public LoginResponse(String username, boolean isAdmin) {
        this.username = username;
        this.isAdmin = isAdmin;
    }

    public String getUsername() {
        return username;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
