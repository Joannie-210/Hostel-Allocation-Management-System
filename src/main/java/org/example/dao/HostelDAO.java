package org.example.dao;

import org.example.models.Hostel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HostelDAO {
    private static final String DB_URL = "jdbc:sqlite:db/hostel.db";

    // CREATE: add a new hostel
    public void addHostel(Hostel hostel) {
        String sql = "INSERT INTO hostels(name, gender, total_rooms, total_capacity, occupied_beds, warden_name, contact_number, description) " +
                "VALUES(?,?,?,?,?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, hostel.getName());
            pstmt.setString(2, hostel.getGender());
            pstmt.setInt(3, hostel.getTotalRooms());
            pstmt.setInt(4, hostel.getTotalCapacity());
            pstmt.setInt(5, hostel.getOccupiedBeds());
            pstmt.setString(6, hostel.getWardenName());
            pstmt.setString(7, hostel.getContactNumber());
            pstmt.setString(8, hostel.getDescription());

            pstmt.executeUpdate();
            System.out.println("✅ Hostel added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ: get all hostels
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

    // UPDATE: modify a hostel
    public void updateHostel(Hostel hostel) {
        String sql = "UPDATE hostels SET name=?, gender=?, total_rooms=?, total_capacity=?, occupied_beds=?, warden_name=?, contact_number=?, description=? WHERE id=?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, hostel.getName());
            pstmt.setString(2, hostel.getGender());
            pstmt.setInt(3, hostel.getTotalRooms());
            pstmt.setInt(4, hostel.getTotalCapacity());
            pstmt.setInt(5, hostel.getOccupiedBeds());
            pstmt.setString(6, hostel.getWardenName());
            pstmt.setString(7, hostel.getContactNumber());
            pstmt.setString(8, hostel.getDescription());
            pstmt.setInt(9, hostel.getId());

            pstmt.executeUpdate();
            System.out.println("✅ Hostel updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE: remove a hostel
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
