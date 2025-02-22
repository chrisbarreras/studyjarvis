package com.christophertbarrerasconsulting.studyjarvis.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequest {
    private String username;
    private String password;

    public LoginRequest() {}

    public LoginRequest (String username, String password){
        this(0, username, password, false);
    }

    @JsonCreator
    public LoginRequest (@JsonProperty("user_id") int userId, @JsonProperty("username") String username, @JsonProperty("password") String password, @JsonProperty("is_administrator") boolean isAdministrator){
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
