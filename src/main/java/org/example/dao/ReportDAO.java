package org.example.dao;

import org.example.models.Report;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {
    private static final String DB_URL = "jdbc:sqlite:db/hostel.db";

    // Get total summary
    public Report getOverallReport() {
        Report report = new Report();

        String sql = """
            SELECT
                (SELECT COUNT(*) FROM hostels) AS total_hostels,
                (SELECT COUNT(*) FROM rooms) AS total_rooms,
                (SELECT SUM(capacity) FROM rooms) AS total_capacity,
                (SELECT SUM(occupants) FROM rooms) AS occupied_beds
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                report.setTotalHostels(rs.getInt("total_hostels"));
                report.setTotalRooms(rs.getInt("total_rooms"));
                report.setTotalCapacity(rs.getInt("total_capacity"));
                report.setOccupiedBeds(rs.getInt("occupied_beds"));
                report.setAvailableBeds(rs.getInt("total_capacity") - rs.getInt("occupied_beds"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return report;
    }

    // Per-hostel breakdown
    public List<Report> getHostelBreakdown() {
        List<Report> list = new ArrayList<>();

        String sql = """
            SELECT h.id AS hostel_id, h.name AS hostel_name,
                   COUNT(r.id) AS total_rooms,
                   IFNULL(SUM(r.capacity), 0) AS total_capacity,
                   IFNULL(SUM(r.occupants), 0) AS occupied_beds,
                   (IFNULL(SUM(r.capacity), 0) - IFNULL(SUM(r.occupants), 0)) AS available_beds
            FROM hostels h
            LEFT JOIN rooms r ON h.id = r.hostel_id
            GROUP BY h.id, h.name
            ORDER BY h.id
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Report report = new Report();
                report.setHostelId(rs.getInt("hostel_id"));
                report.setHostelName(rs.getString("hostel_name"));
                report.setTotalRooms(rs.getInt("total_rooms"));
                report.setTotalCapacity(rs.getInt("total_capacity"));
                report.setOccupiedBeds(rs.getInt("occupied_beds"));
                report.setAvailableBeds(rs.getInt("available_beds"));
                list.add(report);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
