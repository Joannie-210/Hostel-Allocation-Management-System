package org.example.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegNumberGenerator {

    /**
     * Generates a registration number in the format STU0001, STU0002, etc.
     * Uses the last inserted student's ID to increment.
     */
    public static String generateRegNumber(Connection conn) throws SQLException {
        String regNo = "STU0001"; // default for first student
        String query = "SELECT MAX(id) AS max_id FROM students";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                int lastId = rs.getInt("max_id");
                regNo = String.format("STU%04d", lastId + 1);
            }
        }

        return regNo;
    }
}
