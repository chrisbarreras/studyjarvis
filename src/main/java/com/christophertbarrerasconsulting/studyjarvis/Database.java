package com.christophertbarrerasconsulting.studyjarvis;
import java.sql.*;

class Database {
    private static final String URL = "jdbc:postgresql://localhost/javalinApp";
    private static final String USER = "javalinUser";
    private static final String PASS = "yourpassword";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}