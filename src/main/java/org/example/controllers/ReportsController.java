package org.example.controllers;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.example.dao.ReportsDAO;
import org.example.models.Hostel;

public class ReportsController {

    @FXML private VBox summaryCard, capacityCard, rateCard;
    @FXML private Label lblTotalHostels, lblTotalRooms, lblTotalStudents, lblTotalCapacity, lblOccupiedBeds, lblAvailableBeds, lblAvgOccupancy;

    @FXML private TableView<Hostel> reportTable;
    @FXML private TableColumn<Hostel, String> hostelNameCol;
    @FXML private TableColumn<Hostel, String> genderCol;
    @FXML private TableColumn<Hostel, Integer> totalRoomsCol;
    @FXML private TableColumn<Hostel, Integer> totalCapacityCol;
    @FXML private TableColumn<Hostel, Integer> occupiedBedsCol;
    @FXML private TableColumn<Hostel, Integer> availableBedsCol;
    @FXML private TableColumn<Hostel, Double> occupancyRateCol;
    @FXML private PieChart occupancyChart;

    private ReportsDAO reportsDAO = new ReportsDAO();

    @FXML
    public void initialize() {
        setupTable();
        loadReports();
        fadeIn(summaryCard, capacityCard, rateCard, reportTable, occupancyChart);
    }

    private void setupTable() {
        hostelNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        totalRoomsCol.setCellValueFactory(new PropertyValueFactory<>("totalRooms"));
        totalCapacityCol.setCellValueFactory(new PropertyValueFactory<>("totalCapacity"));
        occupiedBedsCol.setCellValueFactory(new PropertyValueFactory<>("occupiedBeds"));
        availableBedsCol.setCellValueFactory(new PropertyValueFactory<>("availableBeds"));
        occupancyRateCol.setCellValueFactory(new PropertyValueFactory<>("occupancyRate"));
    }

    private void loadReports() {
        ObservableList<Hostel> hostels = FXCollections.observableArrayList(reportsDAO.getHostelReports());
        reportTable.setItems(hostels);

        int totalHostels = hostels.size();
        int totalRooms = hostels.stream().mapToInt(Hostel::getTotalRooms).sum();
        int totalCapacity = hostels.stream().mapToInt(Hostel::getTotalCapacity).sum();
        int occupied = hostels.stream().mapToInt(Hostel::getOccupiedBeds).sum();
        int available = totalCapacity - occupied;
        double avgRate = totalCapacity > 0 ? (occupied * 100.0 / totalCapacity) : 0;

        lblTotalHostels.setText("Total Hostels: " + totalHostels);
        lblTotalRooms.setText("Total Rooms: " + totalRooms);
        lblTotalStudents.setText("Total Students: " + occupied);
        lblTotalCapacity.setText("Total Capacity: " + totalCapacity);
        lblOccupiedBeds.setText("Occupied Beds: " + occupied);
        lblAvailableBeds.setText("Available Beds: " + available);
        lblAvgOccupancy.setText(String.format("Avg Occupancy: %.2f%%", avgRate));

        // Populate PieChart
        ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList();
        for (Hostel h : hostels) {
            chartData.add(new PieChart.Data(h.getName(), h.getOccupancyRate()));
        }
        occupancyChart.setData(chartData);
    }

    private void fadeIn(javafx.scene.Node... nodes) {
        for (javafx.scene.Node node : nodes) {
            FadeTransition fade = new FadeTransition(Duration.millis(800), node);
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.play();
        }
    }
}
