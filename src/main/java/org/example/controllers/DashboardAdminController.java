package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class DashboardAdminController {

    @FXML private Label headerTitle;
    @FXML private StackPane contentArea;

    public void showOverview() {
        headerTitle.setText("Overview");
        contentArea.getChildren().setAll(new Text("📊 Overview of all hostels and users"));
    }

    public void showManageStudents() {
        headerTitle.setText("Manage Students");
        contentArea.getChildren().setAll(new Text("👥 Here you can manage student allocations"));
    }

    public void showManageRooms() {
        headerTitle.setText("Manage Rooms");
        contentArea.getChildren().setAll(new Text("🏠 Manage hostel rooms here"));
    }

    public void showReports() {
        headerTitle.setText("Reports");
        contentArea.getChildren().setAll(new Text("📄 Generate and view reports"));
    }

    public void logout() {
        System.out.println("Logging out...");

    }
}
