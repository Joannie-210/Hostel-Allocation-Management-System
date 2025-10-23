package org.example.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;

public class HostelDatabaseSetup {

    public static void main(String[] args) {
        String schemaPath = "schema.sql"; // path relative to project root

        try {
            // Read entire schema.sql into a single string
            String sql = Files.readString(Paths.get(schemaPath));

            try (Connection conn = DBUtil.getConnection(); Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(sql);
                System.out.println("✅ Database setup completed successfully!");
            }

        } catch (IOException e) {
            System.err.println("❌ Could not read schema file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Error setting up database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
