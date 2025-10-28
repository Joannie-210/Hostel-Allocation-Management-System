package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import org.example.utils.DBUtil;

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
    }

    public void loadData() {
        try (Connection conn = DBUtil.getConnection()) {
            int totalHostels = getSingleValue(conn, "SELECT COUNT(*) FROM hostels");
            int totalRooms = getSingleValue(conn, "SELECT COUNT(*) FROM rooms");
            int totalStudents = getSingleValue(conn, "SELECT COUNT(*) FROM students");
            int allocatedRooms = getSingleValue(conn, "SELECT COUNT(*) FROM rooms WHERE allocated = 1");
            int allocatedPercent = totalRooms == 0 ? 0 : (int)((allocatedRooms * 100.0) / totalRooms);

            lblTotalHostels.setText(String.valueOf(totalHostels));
            lblTotalRooms.setText(String.valueOf(totalRooms));
            lblTotalStudents.setText(String.valueOf(totalStudents));
            lblAllocatedRooms.setText(allocatedPercent + "%");

            // PieCharts
            allocationPieChart.getData().setAll(
                    new PieChart.Data("Allocated", allocatedPercent),
                    new PieChart.Data("Available", 100 - allocatedPercent)
            );

            int male = getSingleValue(conn, "SELECT COUNT(*) FROM students WHERE gender='Male'");
            int female = totalStudents - male;

            studentDistributionChart.getData().setAll(
                    new PieChart.Data("Male", male),
                    new PieChart.Data("Female", female)
            );

            // BarChart
            roomsBarChart.getData().clear();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            String sql = "SELECT id, name FROM hostels ORDER BY id";
            try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int hostelId = rs.getInt("id");
                    String hostelName = rs.getString("name");
                    int roomCount = getSingleValue(conn, "SELECT COUNT(*) FROM rooms WHERE hostel_id = " + hostelId);
                    series.getData().add(new XYChart.Data<>(hostelName, roomCount));
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
