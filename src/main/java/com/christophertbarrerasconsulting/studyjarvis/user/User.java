package com.christophertbarrerasconsulting.studyjarvis.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    private int userId;
    private String username;
    private String password;
    private boolean isAdministrator;

    public User() {}

    public User (String username, String password){
        this(0, username, password, false);
    }

    public User (String username, String password, boolean isAdministrator){
        this(0, username, password, isAdministrator);
    }

    @JsonCreator
    public User (@JsonProperty("user_id") int userId, @JsonProperty("username") String username, @JsonProperty("password") String password, @JsonProperty("is_administrator") boolean isAdministrator){
        setUserId(userId);
        setUsername(username);
        setPassword(password);
        setIsAdministrator(isAdministrator);
    }

    private void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId(){
        return userId;
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

    public boolean getIsAdministrator() { return isAdministrator; }

    private void setIsAdministrator(boolean isAdministrator) {this.isAdministrator = isAdministrator;}
}
