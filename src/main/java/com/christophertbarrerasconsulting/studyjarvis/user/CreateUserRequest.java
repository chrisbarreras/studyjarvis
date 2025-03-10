package com.christophertbarrerasconsulting.studyjarvis.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.javalin.openapi.JsonSchema;

public class CreateUserRequest {
    private String username;
    private String password;
    private boolean isAdministrator;

    public CreateUserRequest() {}

    @JsonCreator
    public CreateUserRequest(@JsonProperty(value = "username") String username, @JsonProperty("password") String password, @JsonProperty("is_administrator") boolean isAdministrator){
        setUsername(username);
        setPassword(password);
        setIsAdministrator(isAdministrator);
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
