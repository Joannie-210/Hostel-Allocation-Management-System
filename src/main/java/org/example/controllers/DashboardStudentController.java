package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class DashboardStudentController {

    @FXML private Label headerTitle;
    @FXML private StackPane contentArea;

    public void showOverview() {
        headerTitle.setText("Overview");
        contentArea.getChildren().setAll(new Text("🎓 Welcome to your dashboard!"));
    }

    public void showRoomDetails() {
        headerTitle.setText("My Room Details");
        contentArea.getChildren().setAll(new Text("🛏️ Your allocated room and roommate info."));
    }

    public void showPaymentStatus() {
        headerTitle.setText("Payment Status");
        contentArea.getChildren().setAll(new Text("💳 View your hostel fee payment status."));
    }

    public void showNotices() {
        headerTitle.setText("Notices");
        contentArea.getChildren().setAll(new Text("📢 Hostel announcements appear here."));
    }

    public void logout() {
        System.out.println("Logging out...");

    }
}
