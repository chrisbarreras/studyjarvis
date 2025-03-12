package com.christophertbarrerasconsulting.studyjarvis.user;

public class LoginResponse {
    private String username;
    private boolean isAdmin;
    private String authToken;

    public LoginResponse() {}

    public LoginResponse(String username, boolean isAdmin, String authToken) {
        this.username = username;
        this.isAdmin = isAdmin;
        this.authToken = authToken;
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

    public String getAuthToken() {
        return authToken;
    }
}
