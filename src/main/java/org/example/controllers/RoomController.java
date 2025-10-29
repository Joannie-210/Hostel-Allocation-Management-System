package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.dao.RoomDAO;
import org.example.models.Room;
import org.example.utils.DataChangeNotifier;


public class RoomController {

    @FXML private TableView<Room> roomTable;
    @FXML private TableColumn<Room, Integer> colId;
    @FXML private TableColumn<Room, String> colRoomNumber;
    @FXML private TableColumn<Room, Integer> colHostelId;
    @FXML private TableColumn<Room, Integer> colCapacity;
    @FXML private TableColumn<Room, Integer> colOccupants;
    @FXML private TableColumn<Room, String> colCreatedAt;

    @FXML private TextField txtRoomNumber, txtHostelId, txtCapacity, txtOccupants;
    @FXML private Button btnAdd, btnEdit, btnDelete, btnClear;

    private RoomDAO roomDAO = new RoomDAO();

    @FXML
    public void initialize() {
        // Set up table columns
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colRoomNumber.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        colHostelId.setCellValueFactory(new PropertyValueFactory<>("hostelId"));
        colCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        colOccupants.setCellValueFactory(new PropertyValueFactory<>("occupants"));
        colCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        loadRooms();

        btnAdd.setOnAction(e -> addRoom());
        btnEdit.setOnAction(e -> editRoom());
        btnDelete.setOnAction(e -> deleteRoom());
        btnClear.setOnAction(e -> clearFields());

        roomTable.setOnMouseClicked(e -> selectRoom());
    }

    private void loadRooms() {
        roomTable.setItems(FXCollections.observableArrayList(roomDAO.getAllRooms()));
    }

    private void addRoom() {
        try {
            String roomNumber = txtRoomNumber.getText();
            int hostelId = Integer.parseInt(txtHostelId.getText());
            int capacity = Integer.parseInt(txtCapacity.getText());
            int occupants = Integer.parseInt(txtOccupants.getText());

            Room room = new Room();
            room.setRoomNumber(roomNumber);
            room.setHostelId(hostelId);
            room.setCapacity(capacity);
            room.setOccupants(occupants);

            roomDAO.addRoom(room);
            loadRooms();
            clearFields();
            DataChangeNotifier.getInstance().notifyDataChanged();
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid numbers for Hostel ID, Capacity, and Occupants.");
        }
    }

    private void editRoom() {
        Room selected = roomTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                selected.setRoomNumber(txtRoomNumber.getText());
                selected.setHostelId(Integer.parseInt(txtHostelId.getText()));
                selected.setCapacity(Integer.parseInt(txtCapacity.getText()));
                selected.setOccupants(Integer.parseInt(txtOccupants.getText()));

                roomDAO.updateRoom(selected);
                loadRooms();
                clearFields();
                DataChangeNotifier.getInstance().notifyDataChanged();
            } catch (NumberFormatException e) {
                showAlert("Input Error", "Please enter valid numbers for Hostel ID, Capacity, and Occupants.");
            }
        } else {
            showAlert("No Selection", "Please select a room to edit.");
        }
    }

    private void deleteRoom() {
        Room selected = roomTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            roomDAO.deleteRoom(selected.getId());
            loadRooms();
            clearFields();
            DataChangeNotifier.getInstance().notifyDataChanged();
        } else {
            showAlert("No Selection", "Please select a room to delete.");
        }
    }

    private void selectRoom() {
        Room selected = roomTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            txtRoomNumber.setText(selected.getRoomNumber());
            txtHostelId.setText(String.valueOf(selected.getHostelId()));
            txtCapacity.setText(String.valueOf(selected.getCapacity()));
            txtOccupants.setText(String.valueOf(selected.getOccupants()));
        }
    }

    private void clearFields() {
        txtRoomNumber.clear();
        txtHostelId.clear();
        txtCapacity.clear();
        txtOccupants.clear();
        roomTable.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
