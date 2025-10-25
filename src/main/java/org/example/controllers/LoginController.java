package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import org.example.dao.UserDAO;
import org.example.models.Student;
import org.example.utils.PasswordUtil;
import org.example.utils.SceneUtil;
import org.example.utils.Session;

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
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String role = roleChoice.getValue();

        if (username.isEmpty() || password.isEmpty() || role == null) {
            messageLabel.setText("❌ Please fill all fields");
            return;
        }

        // Hash the entered password
        String hashed = PasswordUtil.hashPassword(password);

        // Validate login credentials
        boolean valid = UserDAO.validateLogin(username, hashed, role);

        if (valid) {
            messageLabel.setText("✅ Login successful");

            if (role.equals("Student")) {
                Student student = UserDAO.getStudentByUsername(username);

                if (student != null) {
                    Session.setCurrentStudent(student); // store logged-in student
                    SceneUtil.switchScene(event, "/fxml/dashboard_student.fxml");
                } else {
                    messageLabel.setText("❌ Could not load student info");
                }

            } else { // Admin
                Session.clear(); // no student stored for admin
                SceneUtil.switchScene(event, "/fxml/dashboard_admin.fxml");
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
