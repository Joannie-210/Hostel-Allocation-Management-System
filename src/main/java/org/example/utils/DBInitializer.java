package org.example.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBInitializer {

    private static final String DB_FOLDER = "db";
    private static final String DB_FILE = "hostel.db";
    private static final String[] SCHEMA_LOCATIONS = {
            "schema.sql",
            "src/org/example/utils/schema.sql"
    };

    public static void main(String[] args) {
        try {
            // Ensure db folder exists
            Files.createDirectories(Path.of(DB_FOLDER));

            String dbPath = DB_FOLDER + "/" + DB_FILE;
            System.out.println("Database will be created at: " + dbPath);

            // Find schema.sql
            Path schemaPath = null;
            for (String loc : SCHEMA_LOCATIONS) {
                Path path = Path.of(loc);
                if (Files.exists(path)) {
                    schemaPath = path;
                    break;
                }
            }

            if (schemaPath == null) {
                System.err.println("❌ schema.sql not found in expected locations.");
                return;
            }

            System.out.println("Using schema file: " + schemaPath.toAbsolutePath());

            // Read the whole SQL schema
            String sql = Files.readString(schemaPath);

            // Split statements by semicolon that is at the **end of a line**
            String[] statements = sql.split(";\\s*\\r?\\n");

            // Connect to SQLite
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
                 Statement stmt = conn.createStatement()) {

                // Enable foreign keys
                stmt.execute("PRAGMA foreign_keys = ON;");

                // Execute each statement individually
                for (String s : statements) {
                    String trimmed = s.trim();
                    if (!trimmed.isEmpty()) {
                        try {
                            stmt.execute(trimmed + ";");
                            System.out.println("✅ Executed: " + trimmed.split("\\n")[0]);
                        } catch (Exception e) {
                            System.err.println("⚠️ Skipped statement: " + trimmed.split("\\n")[0]);
                            e.printStackTrace();
                        }
                    }
                }

                System.out.println("✅ Database initialized successfully!");
            }

        } catch (IOException e) {
            System.err.println("❌ Failed to read schema file: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
