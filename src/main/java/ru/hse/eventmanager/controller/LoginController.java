package ru.hse.eventmanager.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.mindrot.jbcrypt.BCrypt;
import ru.hse.eventmanager.App;
import ru.hse.eventmanager.model.User;
import ru.hse.eventmanager.model.UserRole;

import javax.persistence.*;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private EntityManager em;

    @FXML
    public void initialize() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ru.hse.eventmanager_PU");
        em = emf.createEntityManager();
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            Query q = em.createNamedQuery("User.findByLogin");
            q.setParameter("login", username);
            User user = (User) q.getSingleResult();

            if (password.equals(user.getPassword())) {
                errorLabel.setText("");
                App.setCurrentUser(user);

                UserRole role = user.getIdUserRole();
                if ("administrator".equals(role.getName())) {
                    App.setRoot("adminPanel");
                } else if ("organizer".equals(role.getName())) {
                    App.setRoot("organizerPanel");
                } else {
                    App.setRoot("participantCatalogue");
                }
            } else {
                errorLabel.setText("Неверный логин или пароль");
            }
        } catch (NoResultException e) {
            errorLabel.setText("Неверный логин или пароль");
        } catch (Exception e) {
            errorLabel.setText("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}