package ru.hse.eventmanager.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.hse.eventmanager.App;

public class AdminController {

    @FXML
    private TableView<UserView> userTable;
    @FXML
    private TableColumn<UserView, String> idColumn;
    @FXML
    private TableColumn<UserView, String> loginColumn;
    @FXML
    private TableColumn<UserView, String> nameColumn;
    @FXML
    private TableColumn<UserView, String> roleColumn;
    
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passField1;
    @FXML
    private PasswordField passField2;
    @FXML
    private ChoiceBox<String> roleChoiceBox;
    @FXML
    private Label errorLabel;

    // Временная модель для отображения (без БД)
    public static class UserView {
        private String id, login, name, role;
        public UserView(String id, String login, String name, String role) {
            this.id = id; this.login = login; this.name = name; this.role = role;
        }
        public String getId() { return id; }
        public String getLogin() { return login; }
        public String getName() { return name; }
        public String getRole() { return role; }
    }

    @FXML
    public void initialize() {
        // Заглушка: заполняем таблицу тестовыми данными
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        
        ObservableList<UserView> users = FXCollections.observableArrayList(
            new UserView("1", "admin", "Админ", "administrator"),
            new UserView("2", "org1", "Иван", "organizer"),
            new UserView("3", "user1", "Петр", "participant")
        );
        userTable.setItems(users);
        
        // Заполняем ChoiceBox
        roleChoiceBox.getItems().addAll("administrator", "organizer", "participant");
    }

    @FXML
    private void onUpdateButtonClicked() {
        errorLabel.setText("Функция обновления (без БД)");
    }

    @FXML
    private void handleBack() {
        try {
            App.setRoot("login");
        } catch (Exception e) {
            errorLabel.setText("Ошибка: " + e.getMessage());
        }
    }
}
