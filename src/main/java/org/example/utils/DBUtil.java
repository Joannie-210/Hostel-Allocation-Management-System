package org.example.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String DB_PATH = "db/hostel.db";
    private static final String URL = "jdbc:sqlite:" + DB_PATH;
    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL);
                System.out.println("✅ Connected to SQLite DB: " + DB_PATH);
            }
        } catch (SQLException e) {
            System.err.println("❌ DB connection error: " + e.getMessage());
        }
        return connection;
    }
}
