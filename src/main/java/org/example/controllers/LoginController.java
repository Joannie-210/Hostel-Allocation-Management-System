package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import org.example.dao.UserDAO;
import org.example.utils.PasswordUtil;
import org.example.utils.SceneUtil;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ChoiceBox<String> roleChoice;
    @FXML private Label messageLabel;

    @FXML
    public void initialize() {
        roleChoice.getItems().addAll("Student", "Admin");
    }

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleChoice.getValue();

        if (username.isEmpty() || password.isEmpty() || role == null) {
            messageLabel.setText("Please fill all fields");
            return;
        }

        String hashed = PasswordUtil.hashPassword(password);
        boolean valid = UserDAO.validateLogin(username, hashed, role);

        if (valid) {
            messageLabel.setText("✅ Login successful");
            if (role.equals("Admin")) {
                SceneUtil.switchScene(event, "/fxml/dashboard_admin.fxml");
            } else {
                SceneUtil.switchScene(event, "/fxml/dashboard_student.fxml");
            }
        } else {
            messageLabel.setText("❌ Invalid credentials");
        }
    }

    @FXML
    public void goToRegister(ActionEvent event) {
        SceneUtil.switchScene(event, "/fxml/register.fxml");
    }
}
