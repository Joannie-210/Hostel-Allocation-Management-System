package org.example.dao;

import org.example.models.Room;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {
    private static final String DB_URL = "jdbc:sqlite:db/hostel.db";

    // CREATE
    public void addRoom(Room room) {
        String sql = "INSERT INTO rooms (room_number, hostel_id, capacity, occupants) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, room.getRoomNumber());
            pstmt.setInt(2, room.getHostelId());
            pstmt.setInt(3, room.getCapacity());
            pstmt.setInt(4, room.getOccupants());

            pstmt.executeUpdate();
            System.out.println("✅ Room added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ all rooms
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Room r = new Room();
                r.setId(rs.getInt("id"));
                r.setRoomNumber(rs.getString("room_number"));
                r.setHostelId(rs.getInt("hostel_id"));
                r.setCapacity(rs.getInt("capacity"));
                r.setOccupants(rs.getInt("occupants"));
                r.setCreatedAt(rs.getString("created_at"));
                rooms.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rooms;
    }

    // UPDATE
    public void updateRoom(Room room) {
        String sql = "UPDATE rooms SET room_number=?, hostel_id=?, capacity=?, occupants=? WHERE id=?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, room.getRoomNumber());
            pstmt.setInt(2, room.getHostelId());
            pstmt.setInt(3, room.getCapacity());
            pstmt.setInt(4, room.getOccupants());
            pstmt.setInt(5, room.getId());

            pstmt.executeUpdate();
            System.out.println("✅ Room updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deleteRoom(int roomId) {
        String sql = "DELETE FROM rooms WHERE id=?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, roomId);
            pstmt.executeUpdate();
            System.out.println("✅ Room deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
