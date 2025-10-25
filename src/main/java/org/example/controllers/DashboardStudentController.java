package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.example.models.Student;
import org.example.utils.Session;

public class DashboardStudentController {

    @FXML private Label headerTitle;
    @FXML private StackPane contentArea;

    @FXML
    public void showOverview() {
        headerTitle.setText("Overview");
        contentArea.getChildren().setAll(new Text("🎓 Welcome to your dashboard!"));
    }

    @FXML
    public void showProfile() {
        headerTitle.setText("Profile");
        Student student = Session.getCurrentStudent();
        if (student != null) {
            String profileInfo = String.format(
                    "🎓 Name: %s\n" +
                            "📧 Email: %s\n" +
                            "📞 Phone: %s\n" +
                            "🏫 Department: %s\n" +
                            "📚 Level: %s\n" +
                            "🛏 Room ID: %s",
                    student.getName(),
                    student.getEmail(),
                    student.getPhone(),
                    student.getDepartment(),
                    student.getLevel(),
                    student.getRoomId() != null ? student.getRoomId() : "Not assigned"
            );
            contentArea.getChildren().setAll(new Text(profileInfo));
        } else {
            contentArea.getChildren().setAll(new Text("⚠️ No student data found."));
        }
    }

    @FXML
    public void showRoomDetails() {
        headerTitle.setText("My Room Details");
        Student student = Session.getCurrentStudent();
        if (student != null && student.getRoomId() != null) {
            contentArea.getChildren().setAll(
                    new Text("🛏️ Room ID: " + student.getRoomId() + "\n" +
                            "👤 Roommate info will be displayed here (if available).")
            );
        } else {
            contentArea.getChildren().setAll(new Text("⚠️ You have not been assigned a room yet."));
        }
    }

    @FXML
    public void showPaymentStatus() {
        headerTitle.setText("Payment Status");
        // TODO: fetch actual payment data dynamically
        contentArea.getChildren().setAll(new Text("💳 View your hostel fee payment status."));
    }

    @FXML
    public void showNotices() {
        headerTitle.setText("Notices");
        // TODO: fetch notices dynamically
        contentArea.getChildren().setAll(new Text("📢 Hostel announcements appear here."));
    }

    @FXML
    public void logout() {
        Session.clear();
        System.out.println("Logging out...");
        // replace with your actual SceneUtil method
        // SceneUtil.switchSceneToLogin(event);
    }
}
