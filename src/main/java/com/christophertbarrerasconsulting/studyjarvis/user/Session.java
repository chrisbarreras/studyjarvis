package com.christophertbarrerasconsulting.studyjarvis.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.christophertbarrerasconsulting.studyjarvis.user.Session;

import java.io.Serializable;
import java.sql.Date;

public class Session implements Serializable {
    private final int sessionId;
    private final int userId;
    private final String uploadedFilesPath;
    private final String extractFolder;
    private final Date sessionCreationDate;
    private final Date lastSessionActivityDate;

    @JsonCreator
    public Session(@JsonProperty("session_id") int sessionId,
                   @JsonProperty("user_id") int userId,
                   @JsonProperty("uploaded_folder") String uploadedFilesPath,
                   @JsonProperty("extraction_folder") String extractFolder,
                   @JsonProperty("session_creation_date") Date sessionCreationDate,
                   @JsonProperty("last_session_activity_date") Date lastSessionActivityDate)
    {
        this.sessionId = sessionId;
        this.userId = userId;
        this.uploadedFilesPath = uploadedFilesPath;
        this.extractFolder = extractFolder;
        this.sessionCreationDate = sessionCreationDate;
        this.lastSessionActivityDate = lastSessionActivityDate;
    }

    @JsonProperty("session_id")
    public int getSessionId(){
        return sessionId;
    }

    @JsonProperty("user_id")
    public int getUserId(){
        return userId;
    }

    @JsonProperty("uploaded_folder")
    public String getUploadedFilesPath(){
        return uploadedFilesPath;
    }

    @JsonProperty("extraction_folder")
    public String getExtractFolder(){
        return extractFolder;
    }

    @JsonProperty("session_creation_date")
    public Date getSessionCreationDate(){
        return sessionCreationDate;
    }

    @JsonProperty("last_session_activity_date")
    public Date getLastSessionActivityDate(){
        return lastSessionActivityDate;
    }
}
