package org.example.controllers;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.example.dao.ReportDAO;
import org.example.models.Report;

public class ReportsController {

    @FXML private Label totalHostelsLabel;
    @FXML private Label totalRoomsLabel;
    @FXML private Label totalCapacityLabel;
    @FXML private Label occupiedBedsLabel;
    @FXML private Label availableBedsLabel;

    @FXML private PieChart bedsPieChart;
    @FXML private TableView<Report> hostelTable;
    @FXML private TableColumn<Report, String> colHostelName;
    @FXML private TableColumn<Report, Integer> colTotalRooms;
    @FXML private TableColumn<Report, Integer> colTotalCapacity;
    @FXML private TableColumn<Report, Integer> colOccupied;
    @FXML private TableColumn<Report, Integer> colAvailable;
    @FXML private VBox contentBox;
    @FXML private Button btnRefresh;

    private final ReportDAO reportDAO = new ReportDAO();

    @FXML
    public void initialize() {
        setupTable();
        loadReport();

        // 🔄 Add refresh button action
        btnRefresh.setOnAction(e -> refreshWithAnimation());
    }

    private void setupTable() {
        colHostelName.setCellValueFactory(new PropertyValueFactory<>("hostelName"));
        colTotalRooms.setCellValueFactory(new PropertyValueFactory<>("totalRooms"));
        colTotalCapacity.setCellValueFactory(new PropertyValueFactory<>("totalCapacity"));
        colOccupied.setCellValueFactory(new PropertyValueFactory<>("occupiedBeds"));
        colAvailable.setCellValueFactory(new PropertyValueFactory<>("availableBeds"));
    }

    private void loadReport() {
        Report summary = reportDAO.getOverallReport();

        totalHostelsLabel.setText(String.valueOf(summary.getTotalHostels()));
        totalRoomsLabel.setText(String.valueOf(summary.getTotalRooms()));
        totalCapacityLabel.setText(String.valueOf(summary.getTotalCapacity()));
        occupiedBedsLabel.setText(String.valueOf(summary.getOccupiedBeds()));
        availableBedsLabel.setText(String.valueOf(summary.getAvailableBeds()));

        bedsPieChart.setData(FXCollections.observableArrayList(
                new PieChart.Data("Occupied Beds", summary.getOccupiedBeds()),
                new PieChart.Data("Available Beds", summary.getAvailableBeds())
        ));

        hostelTable.setItems(FXCollections.observableArrayList(reportDAO.getHostelBreakdown()));
    }

    private void refreshWithAnimation() {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), contentBox);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> {
            loadReport();
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), contentBox);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
        });
        fadeOut.play();
    }
}
