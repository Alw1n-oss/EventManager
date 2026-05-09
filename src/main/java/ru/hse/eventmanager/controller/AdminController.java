package ru.hse.eventmanager.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import ru.hse.eventmanager.App;
import ru.hse.eventmanager.model.User;
import ru.hse.eventmanager.model.UserRole;

import javax.persistence.*;
import java.util.List;

public class AdminController {

    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> idColumn;
    @FXML private TableColumn<User, String> loginColumn;
    @FXML private TableColumn<User, String> nameColumn;
    @FXML private TableColumn<User, String> roleColumn;
    
    @FXML private TextField loginField;
    @FXML private PasswordField passField1;
    @FXML private PasswordField passField2;
    @FXML private ChoiceBox<String> roleChoiceBox;
    @FXML private Label errorLabel;

    private EntityManager em;
    private User selectedUser;
    private ObservableList<User> users = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ru.hse.eventmanager_PU");
        em = emf.createEntityManager();

        idColumn.setCellValueFactory(cell -> 
            new SimpleStringProperty(String.valueOf(cell.getValue().getIdUser())));
        loginColumn.setCellValueFactory(cell -> 
            new SimpleStringProperty(cell.getValue().getLogin()));
        nameColumn.setCellValueFactory(cell -> 
            new SimpleStringProperty(cell.getValue().getName()));
        roleColumn.setCellValueFactory(cell -> {
            UserRole r = cell.getValue().getIdUserRole();
            return new SimpleStringProperty(r != null ? r.getName() : "");
        });

        roleChoiceBox.getItems().addAll("administrator", "organizer", "participant");

        loadUsers();

        userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedUser = newVal;
            if (newVal != null) {
                loginField.setText(newVal.getLogin());
                roleChoiceBox.setValue(newVal.getIdUserRole() != null ? newVal.getIdUserRole().getName() : null);
            }
        });
    }

    private void loadUsers() {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
        users.setAll(query.getResultList());
        userTable.setItems(users);
    }

    @FXML
    private void onUpdateButtonClicked() {
        String login = loginField.getText().trim();
        String pass1 = passField1.getText();
        String pass2 = passField2.getText();
        String roleName = roleChoiceBox.getValue();

        if (login.isEmpty()) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Введите логин");
            return;
        }

        try {
            em.getTransaction().begin();

            User user;
            if (selectedUser != null && selectedUser.getLogin().equals(login)) {
                user = selectedUser;
            } else {
                TypedQuery<User> q = em.createQuery(
                    "SELECT u FROM User u WHERE u.login = :login", User.class);
                q.setParameter("login", login);
                List<User> result = q.getResultList();
                user = result.isEmpty() ? null : result.get(0);
            }

            if (user == null) {
                // Создание нового пользователя
                if (!pass1.equals(pass2) || pass1.isEmpty()) {
                    em.getTransaction().rollback();
                    errorLabel.setTextFill(Color.RED);
                    errorLabel.setText("Пароли не совпадают или пустые");
                    return;
                }

                UserRole role = findRoleByName(roleName);
                if (role == null) {
                    em.getTransaction().rollback();
                    errorLabel.setTextFill(Color.RED);
                    errorLabel.setText("Выберите роль");
                    return;
                }

                user = new User();
                user.setLogin(login);
                user.setPassword(pass1); // Позже заменим на BCrypt
                user.setName(login);
                user.setEmail(login + "@hse.ru");
                user.setIdUserRole(role);
                em.persist(user);

                errorLabel.setTextFill(Color.GREEN);
                errorLabel.setText("Пользователь создан");
            } else {
                // Обновление существующего
                if (!pass1.isEmpty()) {
                    if (!pass1.equals(pass2)) {
                        em.getTransaction().rollback();
                        errorLabel.setTextFill(Color.RED);
                        errorLabel.setText("Пароли не совпадают");
                        return;
                    }
                    user.setPassword(pass1);
                }

                if (roleName != null) {
                    UserRole role = findRoleByName(roleName);
                    if (role != null) user.setIdUserRole(role);
                }

                em.merge(user);
                errorLabel.setTextFill(Color.GREEN);
                errorLabel.setText("Данные обновлены");
            }

            em.getTransaction().commit();
            clearFields();
            loadUsers();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private UserRole findRoleByName(String name) {
        if (name == null) return null;
        TypedQuery<UserRole> q = em.createQuery(
            "SELECT r FROM UserRole r WHERE r.name = :name", UserRole.class);
        q.setParameter("name", name);
        List<UserRole> result = q.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    private void clearFields() {
        loginField.clear();
        passField1.clear();
        passField2.clear();
        roleChoiceBox.setValue(null);
        selectedUser = null;
        userTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleBack() {
        try {
            if (em != null && em.isOpen()) em.close();
            App.setRoot("login");
        } catch (Exception e) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}