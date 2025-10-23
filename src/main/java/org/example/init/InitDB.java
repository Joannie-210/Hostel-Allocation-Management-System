package org.example.init;
import org.example.utils.DBUtil;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Statement;

public class InitDB {
    public static void main(String[] args) {
        String schemaFile = "schema.sql";
        try (Connection conn = DBUtil.getConnection();
             BufferedReader br = new BufferedReader(new FileReader(schemaFile))) {
            StringBuilder sb = new StringBuilder();
            String line;
            Statement st = conn.createStatement();
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("--")) continue;
                sb.append(line).append(" ");
                if (line.endsWith(";")) {
                    String sql = sb.toString();
                    st.execute(sql);
                    sb.setLength(0);
                }
            }
            System.out.println("✅ Schema executed successfully.");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
