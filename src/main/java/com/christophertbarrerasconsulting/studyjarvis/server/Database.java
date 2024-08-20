package com.christophertbarrerasconsulting.studyjarvis.server;

import java.sql.*;

class Database {
    private static final String URL = System.getenv("STUDYJARVIS_DB_URL");
    private static final String USER = System.getenv("STUDYJARVIS_DB_USER");
    private static final String PASS = System.getenv("STUDYJARVIS_DB_PASSWORD");

    public static Connection connect() throws SQLException {
        try {
            // Load the PostgreSQL driver
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL Driver not found!", e);
        }

        // Establish the connection
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
