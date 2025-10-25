package org.example.dao;

import org.example.models.Hostel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HostelDAO {
    private static final String DB_URL = "jdbc:sqlite:db/hostel.db";

    // CREATE
    public void addHostel(Hostel hostel) {
        String sql = "INSERT INTO hostels(name, gender, warden_name, contact_number, description) VALUES(?,?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, hostel.getName());
            pstmt.setString(2, hostel.getGender());
            pstmt.setString(3, hostel.getWardenName());
            pstmt.setString(4, hostel.getContactNumber());
            pstmt.setString(5, hostel.getDescription());

            pstmt.executeUpdate();
            System.out.println("✅ Hostel added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ all hostels
    public List<Hostel> getAllHostels() {
        List<Hostel> hostels = new ArrayList<>();
        String sql = "SELECT * FROM hostels";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Hostel h = new Hostel();
                h.setId(rs.getInt("id"));
                h.setName(rs.getString("name"));
                h.setGender(rs.getString("gender"));
                h.setTotalRooms(rs.getInt("total_rooms"));
                h.setTotalCapacity(rs.getInt("total_capacity"));
                h.setOccupiedBeds(rs.getInt("occupied_beds"));
                h.setWardenName(rs.getString("warden_name"));
                h.setContactNumber(rs.getString("contact_number"));
                h.setDescription(rs.getString("description"));
                hostels.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hostels;
    }

    // UPDATE hostel
    public void updateHostel(Hostel hostel) {
        String sql = "UPDATE hostels SET name=?, gender=?, warden_name=?, contact_number=?, description=? WHERE id=?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, hostel.getName());
            pstmt.setString(2, hostel.getGender());
            pstmt.setString(3, hostel.getWardenName());
            pstmt.setString(4, hostel.getContactNumber());
            pstmt.setString(5, hostel.getDescription());
            pstmt.setInt(6, hostel.getId());

            pstmt.executeUpdate();
            System.out.println("✅ Hostel updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE hostel
    public void deleteHostel(int hostelId) {
        String sql = "DELETE FROM hostels WHERE id=?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, hostelId);
            pstmt.executeUpdate();
            System.out.println("✅ Hostel deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
