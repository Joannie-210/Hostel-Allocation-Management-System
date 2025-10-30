package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.dao.HostelDAO;
import org.example.models.Hostel;




public class HostelController {

    // TableView and columns
    @FXML private TableView<Hostel> hostelTable;
    @FXML private TableColumn<Hostel, Integer> colId;
    @FXML private TableColumn<Hostel, String> colName;
    @FXML private TableColumn<Hostel, String> colGender;
    @FXML private TableColumn<Hostel, Integer> colTotalRooms;
    @FXML private TableColumn<Hostel, Integer> colTotalCapacity;
    @FXML private TableColumn<Hostel, Integer> colOccupiedBeds;
    @FXML private TableColumn<Hostel, String> colWardenName;
    @FXML private TableColumn<Hostel, String> colContactNumber;
    @FXML private TableColumn<Hostel, String> colDescription;

    // Input fields
    @FXML private TextField txtName, txtGender, txtTotalRooms, txtTotalCapacity, txtOccupiedBeds, txtWardenName, txtContactNumber, txtDescription;

    // Buttons
    @FXML private Button btnAdd, btnEdit, btnDelete, btnClear;

    private HostelDAO hostelDAO = new HostelDAO();

    @FXML
    public void initialize() {
        // Initialize TableView columns
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colTotalRooms.setCellValueFactory(new PropertyValueFactory<>("totalRooms"));
        colTotalCapacity.setCellValueFactory(new PropertyValueFactory<>("totalCapacity"));
        colOccupiedBeds.setCellValueFactory(new PropertyValueFactory<>("occupiedBeds"));
        colWardenName.setCellValueFactory(new PropertyValueFactory<>("wardenName"));
        colContactNumber.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Load hostels into TableView
        loadHostels();

        // Button actions
        btnAdd.setOnAction(e -> addHostel());
        btnEdit.setOnAction(e -> editHostel());
        btnDelete.setOnAction(e -> deleteHostel());
        btnClear.setOnAction(e -> clearFields());

        // Table selection
        hostelTable.setOnMouseClicked(e -> selectHostel());
    }

    private void loadHostels() {
        hostelTable.setItems(FXCollections.observableArrayList(hostelDAO.getAllHostels()));
    }

    private void addHostel() {
        try {
            Hostel hostel = new Hostel();
            hostel.setName(txtName.getText());
            hostel.setGender(txtGender.getText());
            hostel.setTotalRooms(Integer.parseInt(txtTotalRooms.getText()));
            hostel.setTotalCapacity(Integer.parseInt(txtTotalCapacity.getText()));
            hostel.setOccupiedBeds(Integer.parseInt(txtOccupiedBeds.getText()));
            hostel.setWardenName(txtWardenName.getText());
            hostel.setContactNumber(txtContactNumber.getText());
            hostel.setDescription(txtDescription.getText());

            hostelDAO.addHostel(hostel);
            loadHostels();
            clearFields();
        } catch (NumberFormatException ex) {
            showAlert("Error", "Please enter valid numbers for Total Rooms, Total Capacity, and Occupied Beds.");
        }
    }

    private void editHostel() {
        Hostel selected = hostelTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                selected.setName(txtName.getText());
                selected.setGender(txtGender.getText());
                selected.setTotalRooms(Integer.parseInt(txtTotalRooms.getText()));
                selected.setTotalCapacity(Integer.parseInt(txtTotalCapacity.getText()));
                selected.setOccupiedBeds(Integer.parseInt(txtOccupiedBeds.getText()));
                selected.setWardenName(txtWardenName.getText());
                selected.setContactNumber(txtContactNumber.getText());
                selected.setDescription(txtDescription.getText());

                hostelDAO.updateHostel(selected);
                loadHostels();
                clearFields();
            } catch (NumberFormatException ex) {
                showAlert("Error", "Please enter valid numbers for Total Rooms, Total Capacity, and Occupied Beds.");
            }
        } else {
            showAlert("Warning", "Please select a hostel to edit.");
        }
    }

    private void deleteHostel() {
        Hostel selected = hostelTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            hostelDAO.deleteHostel(selected.getId());
            loadHostels();
            clearFields();
        } else {
            showAlert("Warning", "Please select a hostel to delete.");
        }
    }

    private void selectHostel() {
        Hostel selected = hostelTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            txtName.setText(selected.getName());
            txtGender.setText(selected.getGender());
            txtTotalRooms.setText(String.valueOf(selected.getTotalRooms()));
            txtTotalCapacity.setText(String.valueOf(selected.getTotalCapacity()));
            txtOccupiedBeds.setText(String.valueOf(selected.getOccupiedBeds()));
            txtWardenName.setText(selected.getWardenName());
            txtContactNumber.setText(selected.getContactNumber());
            txtDescription.setText(selected.getDescription());
        }
    }

    private void clearFields() {
        txtName.clear();
        txtGender.clear();
        txtTotalRooms.clear();
        txtTotalCapacity.clear();
        txtOccupiedBeds.clear();
        txtWardenName.clear();
        txtContactNumber.clear();
        txtDescription.clear();
        hostelTable.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
