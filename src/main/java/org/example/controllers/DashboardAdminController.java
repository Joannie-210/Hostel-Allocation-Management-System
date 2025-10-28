package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.example.utils.DBUtil;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class DashboardAdminController implements Initializable {

    @FXML private Label headerTitle;
    @FXML private StackPane contentArea;
    @FXML private BorderPane rootPane;

    @FXML private PieChart allocationPieChart;
    @FXML private PieChart studentDistributionChart;
    @FXML private Label lblTotalHostels;
    @FXML private Label lblTotalRooms;
    @FXML private Label lblTotalStudents;
    @FXML private Label lblAllocatedRooms;

    @FXML private BarChart<String, Number> roomsBarChart;
    @FXML private VBox overviewBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Apply CSS
        rootPane.getStylesheets().add(getClass().getResource("/css/dashboard.css").toExternalForm());

        // Show Overview on startup
        showOverview();
    }

    // -------------------- Overview --------------------
    public void showOverview() {
        headerTitle.setText("Overview");
        try {
            Node node = FXMLLoader.load(getClass().getResource("/fxml/overview.fxml"));
            contentArea.getChildren().setAll(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // -------------------- Charts --------------------
    private void updateCharts(Connection conn, int totalStudents, int allocatedRoomsPercentage) throws Exception {
        // Allocation PieChart
        allocationPieChart.getData().clear();
        allocationPieChart.getData().addAll(
                new PieChart.Data("Allocated", allocatedRoomsPercentage),
                new PieChart.Data("Available", 100 - allocatedRoomsPercentage)
        );

        // Student Distribution PieChart
        int male = getMaleStudents(conn);
        int female = totalStudents - male;
        studentDistributionChart.getData().clear();
        studentDistributionChart.getData().addAll(
                new PieChart.Data("Male", male),
                new PieChart.Data("Female", female)
        );

        // Rooms per Hostel BarChart
        roomsBarChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        String sql = "SELECT id, name FROM hostels ORDER BY id";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int hostelId = rs.getInt("id");
                String hostelName = rs.getString("name");
                series.getData().add(new XYChart.Data<>(hostelName, getRoomsPerHostel(conn, hostelId)));
            }
        }
        roomsBarChart.getData().add(series);
    }

    // -------------------- Database Methods --------------------
    private int getTotalHostels(Connection conn) throws Exception {
        return getSingleValue(conn, "SELECT COUNT(*) FROM hostels");
    }

    private int getTotalRooms(Connection conn) throws Exception {
        return getSingleValue(conn, "SELECT COUNT(*) FROM rooms");
    }

    private int getTotalStudents(Connection conn) throws Exception {
        return getSingleValue(conn, "SELECT COUNT(*) FROM students");
    }

    private int getAllocatedRoomsPercentage(Connection conn) throws Exception {
        int allocated = getSingleValue(conn, "SELECT COUNT(*) FROM rooms WHERE allocated = 1");
        int total = getTotalRooms(conn);
        return total == 0 ? 0 : (int)((allocated * 100.0) / total);
    }

    private int getMaleStudents(Connection conn) throws Exception {
        return getSingleValue(conn, "SELECT COUNT(*) FROM students WHERE gender = 'Male'");
    }

    private int getRoomsPerHostel(Connection conn, int hostelId) throws Exception {
        String sql = "SELECT COUNT(*) FROM rooms WHERE hostel_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, hostelId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    private int getSingleValue(Connection conn, String query) throws Exception {
        try (PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    // -------------------- Sidebar actions --------------------
    public void showManageStudents() { headerTitle.setText("Manage Students"); loadContent("/fxml/AdminStudents.fxml"); }
    public void showManageHostels() { headerTitle.setText("Manage Hostels"); loadContent("/fxml/HostelManagement.fxml"); }
    public void showManageRooms() { headerTitle.setText("Manage Rooms"); loadContent("/fxml/RoomManagement.fxml"); }
    public void showAllocateHostels() { headerTitle.setText("Allocate Hostels"); loadContent("/fxml/allocate_room.fxml"); }
    public void showReports() { headerTitle.setText("Reports"); contentArea.getChildren().setAll(new Text("📄 Generate and view reports")); }
    public void logout() { System.out.println("Logging out..."); /* TODO: switch scene */ }

    // -------------------- FXML loader --------------------
    private void loadContent(String fxmlPath) {
        try {
            Node node = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentArea.getChildren().setAll(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
