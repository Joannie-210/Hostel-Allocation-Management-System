package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import org.example.dao.StudentDAO;
import org.example.models.Student;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminStudentsController {

    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, String> regNoCol;
    @FXML private TableColumn<Student, String> nameCol;
    @FXML private TableColumn<Student, String> emailCol;
    @FXML private TableColumn<Student, String> genderCol;
    @FXML private TableColumn<Student, String> phoneCol;
    @FXML private TableColumn<Student, String> departmentCol;
    @FXML private TableColumn<Student, String> levelCol;
    @FXML private TableColumn<Student, Integer> roomCol;
    @FXML private TableColumn<Student, Void> deleteCol; // for Delete button

    @FXML
    public void initialize() {
        // Set table column mappings
        regNoCol.setCellValueFactory(new PropertyValueFactory<>("regNo"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        departmentCol.setCellValueFactory(new PropertyValueFactory<>("department"));
        levelCol.setCellValueFactory(new PropertyValueFactory<>("level"));
        roomCol.setCellValueFactory(new PropertyValueFactory<>("roomId"));

        // Load students from DB
        studentTable.setItems(FXCollections.observableArrayList(StudentDAO.getAllStudents()));
        studentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Add Delete button column
        addDeleteButtonToTable();
    }

    private void addDeleteButtonToTable() {
        Callback<TableColumn<Student, Void>, TableCell<Student, Void>> cellFactory = param -> new TableCell<>() {
            private final Button btn = new Button("Delete");

            {
                btn.setStyle("-fx-background-color: #e53935; -fx-text-fill: white; -fx-background-radius: 5;");
                btn.setOnAction(event -> {
                    Student student = getTableView().getItems().get(getIndex());
                    // Delete student from database
                    boolean deleted = StudentDAO.deleteStudent(student.getRegNo());
                    if (deleted) {
                        // Remove student from table view
                        getTableView().getItems().remove(student);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        };

        deleteCol.setCellFactory(cellFactory);
    }
}
