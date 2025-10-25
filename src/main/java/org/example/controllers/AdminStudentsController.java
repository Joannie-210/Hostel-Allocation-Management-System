package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.dao.StudentDAO;
import org.example.models.Student;

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
    @FXML private TableColumn<Student, String> roleCol;

    @FXML
    public void initialize() {
        regNoCol.setCellValueFactory(new PropertyValueFactory<>("regNo"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        departmentCol.setCellValueFactory(new PropertyValueFactory<>("department"));
        levelCol.setCellValueFactory(new PropertyValueFactory<>("level"));
        roomCol.setCellValueFactory(new PropertyValueFactory<>("roomId"));

        studentTable.setItems(FXCollections.observableArrayList(StudentDAO.getAllStudents()));
    }
}
