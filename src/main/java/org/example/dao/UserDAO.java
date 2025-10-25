package org.example.dao;

import org.example.utils.DBUtil;
import org.example.models.Student;
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
    public static Student getStudentByUsername(String username) {
        String sql = "SELECT * FROM students WHERE email=? OR reg_no=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, username);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Student(
                        rs.getInt("id"),           // Integer id
                        rs.getString("reg_no"),    // String regNo
                        rs.getString("name"),      // String name
                        rs.getString("email"),     // String email
                        rs.getString("gender"),    // String gender
                        rs.getString("level"),     // String level
                        rs.getString("phone"),     // String phone
                        rs.getString("department"),// String department
                        rs.getString("role"),      // String role
                         // String password
                        rs.getInt("room_id") != 0 ? rs.getInt("room_id") : null, // Integer roomId
                        rs.getString("password")
                );

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
