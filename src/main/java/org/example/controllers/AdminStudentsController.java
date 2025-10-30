package org.example.controllers;

import org.example.utils.DataChangeNotifier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
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
    @FXML private TableColumn<Student, Void> deleteCol;

    @FXML private TextField searchField; // make sure fx:id="searchField" in your FXML

    private ObservableList<Student> masterList;

    @FXML
    public void initialize() {
        // Set up table columns
        regNoCol.setCellValueFactory(new PropertyValueFactory<>("regNo"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        departmentCol.setCellValueFactory(new PropertyValueFactory<>("department"));
        levelCol.setCellValueFactory(new PropertyValueFactory<>("level"));
        roomCol.setCellValueFactory(new PropertyValueFactory<>("roomId"));

        // Load students from DB (excluding admins)
        masterList = FXCollections.observableArrayList(
                StudentDAO.getAllStudents()
                        .stream()
                        .filter(s -> !"admin".equalsIgnoreCase(s.getRole()))
                        .toList()
        );

        // Wrap list in FilteredList for searching
        FilteredList<Student> filteredData = new FilteredList<>(masterList, p -> true);

        // Add listener for search
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(student -> {
                if (newValue == null || newValue.isBlank()) return true;

                String lowerCaseFilter = newValue.toLowerCase();

                // Search by multiple fields
                return student.getName().toLowerCase().contains(lowerCaseFilter)
                        || student.getRegNo().toLowerCase().contains(lowerCaseFilter)
                        || student.getDepartment().toLowerCase().contains(lowerCaseFilter)
                        || student.getEmail().toLowerCase().contains(lowerCaseFilter)
                        || student.getPhone().toLowerCase().contains(lowerCaseFilter);
            });
        });

        // Wrap filtered list in a SortedList for sorting
        SortedList<Student> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(studentTable.comparatorProperty());

        // Apply to table
        studentTable.setItems(sortedData);
        studentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Add Delete button
        addDeleteButtonToTable();
    }

    private void addDeleteButtonToTable() {
        Callback<TableColumn<Student, Void>, TableCell<Student, Void>> cellFactory = param -> new TableCell<>() {
            private final Button btn = new Button("Delete");

            {
                btn.setStyle("-fx-background-color: #e53935; -fx-text-fill: white; -fx-background-radius: 5;");
                btn.setOnAction(event -> {
                    Student student = getTableView().getItems().get(getIndex());
                    boolean deleted = StudentDAO.deleteStudent(student.getRegNo());
                    if (deleted) {
                        masterList.remove(student); // update main list
                        DataChangeNotifier.getInstance().notifyDataChanged();
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
