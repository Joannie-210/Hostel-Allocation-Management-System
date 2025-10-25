package org.example.dao;

import org.example.utils.DBUtil;
import java.sql.*;

public class UserDAO {

    public static boolean validateLogin(String username, String password, String role) {
        String query = "SELECT * FROM students WHERE (email = ? OR reg_no = ?) AND password = ? AND role = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.setString(4, role);

            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean registerUser(String name, String email, String regNo, String password, String gender, String role, String phone, String department) {
        String sql = "INSERT INTO students (name, email, reg_no, password, gender, role, phone, department) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, regNo);
            stmt.setString(4, password);
            stmt.setString(5, gender);
            stmt.setString(6, role);
            stmt.setString(7, phone);
            stmt.setString(8, department);

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
