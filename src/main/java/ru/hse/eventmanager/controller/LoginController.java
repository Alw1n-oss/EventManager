package ru.hse.eventmanager.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ru.hse.eventmanager.App;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Временная заглушка без БД
        if ("admin".equals(username) && "admin".equals(password)) {
            try {
                App.setRoot("adminPanel");
        } catch (Exception ex) {
                errorLabel.setText("Ошибка перехода: " + ex.getMessage());
        }
            // Через 1 секунду перейдём на adminPanel (когда создадим его)
            // Пока просто показываем текст
        } else if ("org".equals(username) && "org".equals(password)) {
            try {
                App.setRoot("organizerPanel");
        } catch (Exception ex) {
                errorLabel.setText("Ошибка перехода: " + ex.getMessage());
        }
        } else if ("user".equals(username) && "user".equals(password)) {
            try {
                App.setRoot("participantCatalogue");
        } catch (Exception ex) {
                errorLabel.setText("Ошибка перехода: " + ex.getMessage());
        }
        } else {
                errorLabel.setText("Неверный логин или пароль");
        }
        
    }
}