package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import org.example.dao.UserDAO;
import org.example.utils.PasswordUtil;
import org.example.utils.SceneUtil;
import org.example.utils.DBUtil;
import org.example.utils.RegNumberGenerator;

import java.sql.Connection;

public class RegisterController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    // Removed regNoField — now generated automatically
    @FXML private PasswordField passwordField;
    @FXML private ChoiceBox<String> genderChoice;
    @FXML private ChoiceBox<String> roleChoice;
    @FXML private Label messageLabel;

    @FXML
    public void initialize() {
        genderChoice.getItems().addAll("Male", "Female");
        roleChoice.getItems().addAll("Student", "Admin");
    }

    @FXML
    public void handleRegister(ActionEvent event) {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String gender = genderChoice.getValue();
        String role = roleChoice.getValue();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || gender == null || role == null) {
            messageLabel.setText("Please fill all fields");
            return;
        }

        try (Connection conn = DBUtil.getConnection()) {
            // ✅ Generate registration number automatically
            String regNo = RegNumberGenerator.generateRegNumber(conn);

            String hashed = PasswordUtil.hashPassword(password);
            boolean success = UserDAO.registerUser(name, email, regNo, hashed, gender, role);

            if (success) {
                messageLabel.setText("✅ Registration successful. Your Reg No: " + regNo);
                SceneUtil.switchScene(event, "/fxml/login.fxml");
            } else {
                messageLabel.setText("❌ Registration failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("❌ Error: " + e.getMessage());
        }
    }

    @FXML
    public void goToLogin(ActionEvent event) {
        SceneUtil.switchScene(event, "/fxml/login.fxml");
    }
}
