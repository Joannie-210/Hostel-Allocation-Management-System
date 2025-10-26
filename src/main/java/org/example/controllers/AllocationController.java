package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.dao.*;
import org.example.models.*;
import java.sql.*;
import java.util.*;

public class AllocationController {
    @FXML private ComboBox<Student> studentCombo;
    @FXML private ComboBox<Room> roomCombo;
    @FXML private Button allocateBtn;
    @FXML private TableView<Map<String, String>> allocationTable;
    @FXML private TableColumn<Map<String, String>, String> studentCol;
    @FXML private TableColumn<Map<String, String>, String> roomCol;
    @FXML private TableColumn<Map<String, String>, String> dateCol;

    private final AllocationDAO allocationDAO = new AllocationDAO();
    private final RoomDAO roomDAO = new RoomDAO();
    private final StudentDAO studentDAO = new StudentDAO();

    @FXML
    public void initialize() {
        loadStudents();
        loadRooms();
        loadAllocations();
        allocateBtn.setOnAction(e -> allocateRoom());

        studentCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().get("student")));
        roomCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().get("room")));
        dateCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().get("date")));
    }

    private void loadStudents() {
        List<Student> all = StudentDAO.getAllStudents();
        List<Student> unallocated = all.stream().filter(s -> s.getRoomId() == null).toList();
        studentCombo.setItems(FXCollections.observableArrayList(unallocated));
        studentCombo.setConverter(new javafx.util.StringConverter<>() {
            public String toString(Student s) { return s == null ? "" : s.getName(); }
            public Student fromString(String s) { return null; }
        });
    }

    private void loadRooms() {
        List<Room> rooms = roomDAO.getAllRooms();
        List<Room> available = rooms.stream().filter(Room::isAvailable).toList();
        roomCombo.setItems(FXCollections.observableArrayList(available));
        roomCombo.setConverter(new javafx.util.StringConverter<>() {
            public String toString(Room r) { return r == null ? "" : r.getRoomNumber(); }
            public Room fromString(String s) { return null; }
        });
    }

    private void allocateRoom() {
        Student student = studentCombo.getValue();
        Room room = roomCombo.getValue();

        if (student == null || room == null) {
            showAlert("Please select both a student and a room.");
            return;
        }

        boolean success = allocationDAO.allocateRoom(student.getId(), room.getId());
        if (success) {
            showAlert("✅ Allocation successful!");
            loadStudents();
            loadRooms();
            loadAllocations();
        } else {
            showAlert("❌ Allocation failed — room may be full or student already assigned.");
        }
    }

    private void loadAllocations() {
        List<Map<String, String>> rows = new ArrayList<>();
        try (ResultSet rs = allocationDAO.getAllocations()) {
            while (rs.next()) {
                Map<String, String> row = new HashMap<>();
                row.put("student", rs.getString("student_name"));
                row.put("room", rs.getString("room_number"));
                row.put("date", rs.getString("date_allocated"));
                rows.add(row);
            }
            allocationTable.setItems(FXCollections.observableArrayList(rows));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
