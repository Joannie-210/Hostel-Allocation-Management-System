package org.example.dao;

import org.example.models.Student;
import org.example.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public static boolean updateStudent(Student s) {
        String sql = "UPDATE students SET name=?, email=?, gender=?, level=?, phone=?, department=?, room_id=?, password=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, s.getName());
            ps.setString(2, s.getEmail());
            ps.setString(3, s.getGender());
            ps.setString(4, s.getLevel());
            ps.setString(5, s.getPhone());
            ps.setString(6, s.getDepartment());
            if (s.getRoomId() != null)
                ps.setInt(7, s.getRoomId());
            else
                ps.setNull(7, Types.INTEGER);

            // password field may be null if student did not change it
            if (s.getPassword() != null && !s.getPassword().isEmpty())
                ps.setString(8, s.getPassword());
            else {
                // Keep current password unchanged
                ps.setNull(8, Types.VARCHAR);
            }

            ps.setInt(9, s.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY name ASC";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("id"),
                        rs.getString("reg_no"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("gender"),
                        rs.getString("level"),
                        rs.getString("phone"),
                        rs.getString("department"),
                        rs.getString("role"),
                        rs.getInt("room_id") != 0 ? rs.getInt("room_id") : null,
                        rs.getString("password")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public static Student getStudentById(int id) {
        String sql = "SELECT * FROM students WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Student(
                        rs.getInt("id"),
                        rs.getString("reg_no"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("gender"),
                        rs.getString("level"),
                        rs.getString("phone"),
                        rs.getString("department"),
                        rs.getString("role"),
                        rs.getInt("room_id") != 0 ? rs.getInt("room_id") : null,
                        rs.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }
    public static boolean deleteStudent(String regNo) {
        String query = "DELETE FROM students WHERE regNo = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, regNo);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // true if deletion succeeded
        } catch (SQLException e) {
            System.err.println("❌ Error deleting student: " + e.getMessage());
            return false;
        }
    }


}
