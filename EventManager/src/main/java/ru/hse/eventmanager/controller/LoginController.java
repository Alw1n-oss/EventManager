package ru.hse.eventmanager.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ru.hse.eventmanager.App;
import ru.hse.eventmanager.dto.UserDTO;
import ru.hse.eventmanager.service.ApiService;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final ApiService apiService = new ApiService();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            UserDTO user = apiService.login(username, password);
            errorLabel.setText("");
            ApiService.setCurrentUser(user);

            String role = user.getRoleName();
            if ("administrator".equals(role)) {
                App.setRoot("adminPanel");
            } else if ("organizer".equals(role)) {
                App.setRoot("organizerPanel");
            } else {
                App.setRoot("participantCatalogue");
            }
            } catch (Exception e) {
                errorLabel.setText("Ошибка: " + e.getMessage());  // было "Неверный логин или пароль"
                e.printStackTrace();
            }
    }
}