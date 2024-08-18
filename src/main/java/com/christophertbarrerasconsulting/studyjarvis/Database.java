package com.christophertbarrerasconsulting.studyjarvis;
import java.sql.*;

class Database {
    private static final String URL = "jdbc:postgresql://localhost/studyJarvisServer";
    private static final String USER = System.getenv("STUDYJARVIS_DB_USER");
    private static final String PASS = System.getenv("STUDYJARVIS_DB_PASSWORD");

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}