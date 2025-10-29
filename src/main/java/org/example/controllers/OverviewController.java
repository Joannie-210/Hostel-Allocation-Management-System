package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import org.example.utils.DBUtil;
import org.example.utils.DataChangeNotifier;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class OverviewController implements Initializable {

    @FXML private Label lblTotalHostels;
    @FXML private Label lblTotalRooms;
    @FXML private Label lblTotalStudents;
    @FXML private Label lblAllocatedRooms;

    @FXML private PieChart allocationPieChart;
    @FXML private PieChart studentDistributionChart;
    @FXML private BarChart<String, Number> roomsBarChart;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();

        // 🔁 Automatically refresh when other controllers change data
        DataChangeNotifier.getInstance().addListener(this::loadData);
    }

    public void loadData() {
        try (Connection conn = DBUtil.getConnection()) {

            // --- Fetch basic totals ---
            int totalHostels = getSingleValue(conn, "SELECT COUNT(*) FROM hostels");
            int totalRooms = getSingleValue(conn, "SELECT COUNT(*) FROM rooms");
            int totalStudents = getSingleValue(conn, "SELECT COUNT(*) FROM students WHERE role = 'Student'");
            int allocatedRooms = getSingleValue(conn, "SELECT COUNT(DISTINCT room_id) FROM allocations");
            int male = getSingleValue(conn, "SELECT COUNT(*) FROM students WHERE gender='Male' AND role='Student'");
            int female = totalStudents - male;

            int allocatedPercent = (totalRooms == 0) ? 0 : (int)((allocatedRooms * 100.0) / totalRooms);

            // --- Update labels ---
            lblTotalHostels.setText(String.valueOf(totalHostels));
            lblTotalRooms.setText(String.valueOf(totalRooms));
            lblTotalStudents.setText(String.valueOf(totalStudents));
            lblAllocatedRooms.setText(allocatedPercent + "%");

            // --- Update PieChart: Room Allocation ---
            allocationPieChart.getData().setAll(
                    new PieChart.Data("Allocated Rooms", allocatedRooms),
                    new PieChart.Data("Available Rooms", totalRooms - allocatedRooms)
            );

            // --- Update PieChart: Student Gender Distribution ---
            studentDistributionChart.getData().setAll(
                    new PieChart.Data("Male Students", male),
                    new PieChart.Data("Female Students", female)
            );

            // --- Update BarChart: Rooms per Hostel ---
            roomsBarChart.getData().clear();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Rooms per Hostel");

            String sql = "SELECT hostel_name, total_rooms FROM hostel_dashboard ORDER BY hostel_name";
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    series.getData().add(new XYChart.Data<>(
                            rs.getString("hostel_name"),
                            rs.getInt("total_rooms")
                    ));
                }
            }

            roomsBarChart.getData().add(series);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getSingleValue(Connection conn, String query) throws Exception {
        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }
}
