package com.christophertbarrerasconsulting.studyjarvis.user;

public class User {
    private String username;
    private String password;

    public User() {}

    public User (String username, String password){
        setUsername(username);
        setPassword(password);
    }

    public String getUsername() {
        return username;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    private void setPassword(String password) {
        this.password = password;
    }
}
