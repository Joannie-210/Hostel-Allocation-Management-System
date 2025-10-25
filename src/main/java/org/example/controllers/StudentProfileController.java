package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.dao.StudentDAO;
import org.example.models.Student;
import org.example.utils.Session;
import org.example.utils.PasswordUtil;

public class StudentProfileController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField departmentField;
    @FXML private TextField levelField;
    @FXML private PasswordField passwordField;
    @FXML private ChoiceBox<String> genderChoice;
    @FXML private Label messageLabel;

    private Student currentStudent;

    @FXML
    public void initialize() {
        genderChoice.getItems().addAll("Male", "Female");
        loadStudent();
    }

    private void loadStudent() {
        // ✅ Get the logged-in student from the session
        currentStudent = Session.getCurrentStudent();

        if (currentStudent != null) {
            nameField.setText(currentStudent.getName());
            emailField.setText(currentStudent.getEmail());
            phoneField.setText(currentStudent.getPhone());
            departmentField.setText(currentStudent.getDepartment());
            levelField.setText(currentStudent.getLevel());
            genderChoice.setValue(currentStudent.getGender());
        }
    }

    @FXML
    public void handleUpdate() {
        // Update fields
        currentStudent.setName(nameField.getText());
        currentStudent.setEmail(emailField.getText());
        currentStudent.setPhone(phoneField.getText());
        currentStudent.setDepartment(departmentField.getText());
        currentStudent.setLevel(levelField.getText());
        currentStudent.setGender(genderChoice.getValue());

        // Optional: update password if field is not empty
        String newPassword = passwordField.getText();
        if (newPassword != null && !newPassword.isEmpty()) {
            currentStudent.setPassword(PasswordUtil.hashPassword(newPassword));
        }

        boolean success = StudentDAO.updateStudent(currentStudent);
        messageLabel.setText(success ? "✅ Profile updated" : "❌ Update failed");
    }
}
