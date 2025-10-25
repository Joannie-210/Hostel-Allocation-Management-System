package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.dao.RoomDAO;
import org.example.models.Room;

public class RoomController {

    @FXML private TableView<Room> roomTable;
    @FXML private TableColumn<Room, Integer> colId;
    @FXML private TableColumn<Room, String> colRoomNumber;
    @FXML private TableColumn<Room, Integer> colHostelId;
    @FXML private TableColumn<Room, String> colType;
    @FXML private TableColumn<Room, Boolean> colAvailable;

    @FXML private TextField txtRoomNumber, txtHostelId, txtType;
    @FXML private CheckBox chkAvailable;
    @FXML private Button btnAdd, btnEdit, btnDelete, btnClear;

    private RoomDAO roomDAO = new RoomDAO();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colRoomNumber.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        colHostelId.setCellValueFactory(new PropertyValueFactory<>("hostelId"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colAvailable.setCellValueFactory(new PropertyValueFactory<>("available"));

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
        Room room = new Room(0, txtRoomNumber.getText(),
                Integer.parseInt(txtHostelId.getText()),
                txtType.getText(),
                chkAvailable.isSelected());
        roomDAO.addRoom(room);
        loadRooms();
        clearFields();
    }

    private void editRoom() {
        Room selected = roomTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setRoomNumber(txtRoomNumber.getText());
            selected.setHostelId(Integer.parseInt(txtHostelId.getText()));
            selected.setType(txtType.getText());
            selected.setAvailable(chkAvailable.isSelected());
            roomDAO.updateRoom(selected);
            loadRooms();
            clearFields();
        }
    }

    private void deleteRoom() {
        Room selected = roomTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            roomDAO.deleteRoom(selected.getId());
            loadRooms();
            clearFields();
        }
    }

    private void selectRoom() {
        Room selected = roomTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            txtRoomNumber.setText(selected.getRoomNumber());
            txtHostelId.setText(String.valueOf(selected.getHostelId()));
            txtType.setText(selected.getType());
            chkAvailable.setSelected(selected.isAvailable());
        }
    }

    private void clearFields() {
        txtRoomNumber.clear();
        txtHostelId.clear();
        txtType.clear();
        chkAvailable.setSelected(false);
        roomTable.getSelectionModel().clearSelection();
    }
}
