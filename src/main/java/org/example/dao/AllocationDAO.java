package org.example.dao;

import org.example.utils.DBUtil;
import java.sql.*;

public class AllocationDAO {

    // allocate student to room (transaction)
    public boolean allocateRoom(int studentId, int roomId) {
        String updateRoom = "UPDATE rooms SET occupants = occupants + 1 WHERE id = ? AND occupants < capacity";
        String updateStudent = "UPDATE students SET room_id = ? WHERE id = ? AND (room_id IS NULL OR room_id = 0)";
        String insertAllocation = "INSERT INTO allocations (student_id, room_id, date_allocated) VALUES (?, ?, CURRENT_TIMESTAMP)";

        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psRoom = conn.prepareStatement(updateRoom);
                 PreparedStatement psStudent = conn.prepareStatement(updateStudent);
                 PreparedStatement psAlloc = conn.prepareStatement(insertAllocation)) {

                // 1️⃣ update room (occupants + 1)
                psRoom.setInt(1, roomId);
                int roomUpdated = psRoom.executeUpdate();
                if (roomUpdated == 0) {
                    conn.rollback();
                    System.out.println("❌ Room full or not found.");
                    return false;
                }

                // 2️⃣ assign student to room
                psStudent.setInt(1, roomId);
                psStudent.setInt(2, studentId);
                int studentUpdated = psStudent.executeUpdate();
                if (studentUpdated == 0) {
                    conn.rollback();
                    System.out.println("❌ Student not found or already allocated.");
                    return false;
                }

                // 3️⃣ record allocation
                psAlloc.setInt(1, studentId);
                psAlloc.setInt(2, roomId);
                psAlloc.executeUpdate();

                conn.commit();
                System.out.println("✅ Student allocated successfully.");
                return true;

            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // get all allocations
    public ResultSet getAllocations() throws SQLException {
        String sql = """
                SELECT a.id, s.name AS student_name, r.room_number, a.date_allocated
                FROM allocations a
                JOIN students s ON a.student_id = s.id
                JOIN rooms r ON a.room_id = r.id
                ORDER BY a.date_allocated DESC
                """;
        Connection conn = DBUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        return ps.executeQuery();
    }
}
