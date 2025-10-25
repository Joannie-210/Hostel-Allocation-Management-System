package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class DashboardAdminController {

    @FXML private Label headerTitle;
    @FXML private StackPane contentArea;

    // -------------------- Sidebar actions --------------------
    public void showOverview() {
        headerTitle.setText("Overview");
        contentArea.getChildren().setAll(new Text("📊 Overview of all hostels and users"));
    }

    public void showManageStudents() {
        headerTitle.setText("Manage Students");
        loadContent("/fxml/AdminStudents.fxml");
    }


    public void showManageHostels() {
        headerTitle.setText("Manage Hostels");
        loadContent("/fxml/HostelManagement.fxml");
    }

    public void showManageRooms() {
        headerTitle.setText("Manage Rooms");
        loadContent("/fxml/RoomManagement.fxml");
    }

    public void showReports() {
        headerTitle.setText("Reports");
        contentArea.getChildren().setAll(new Text("📄 Generate and view reports"));
    }

    public void logout() {
        System.out.println("Logging out...");
        // TODO: Add logic to switch back to login scene
    }

    // -------------------- FXML loader --------------------
    private void loadContent(String fxmlPath) {
        try {
            System.out.println("Trying to load: " + fxmlPath);
            System.out.println("Resolved URL: " + getClass().getResource(fxmlPath));

            Node node = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentArea.getChildren().setAll(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
