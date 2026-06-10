package ru.hse.eventmanager.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import ru.hse.eventmanager.App;
import ru.hse.eventmanager.dto.UserDTO;
import ru.hse.eventmanager.service.ApiService;

public class AdminController {

    @FXML private TableView<UserDTO> userTable;
    @FXML private TableColumn<UserDTO, String> idColumn;
    @FXML private TableColumn<UserDTO, String> loginColumn;
    @FXML private TableColumn<UserDTO, String> nameColumn;
    @FXML private TableColumn<UserDTO, String> roleColumn;
    
    @FXML private TextField loginField;
    @FXML private PasswordField passField1;
    @FXML private PasswordField passField2;
    @FXML private ChoiceBox<String> roleChoiceBox;
    @FXML private Label errorLabel;

    private final ApiService apiService = new ApiService();
    private UserDTO selectedUser;

    @FXML
    public void initialize() {
        roleChoiceBox.getItems().addAll("administrator", "organizer", "participant");
        
        idColumn.setCellValueFactory(cell -> 
            new SimpleStringProperty(cell.getValue().getIdUser() != null ? cell.getValue().getIdUser().toString() : ""));
        loginColumn.setCellValueFactory(cell -> 
            new SimpleStringProperty(cell.getValue().getLogin()));
        nameColumn.setCellValueFactory(cell -> 
            new SimpleStringProperty(cell.getValue().getName()));
        roleColumn.setCellValueFactory(cell -> 
            new SimpleStringProperty(cell.getValue().getRoleName()));
        
        loadUsers();
        
        userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedUser = newVal;
                loginField.setText(newVal.getLogin());
                roleChoiceBox.setValue(newVal.getRoleName());
                passField1.clear();
                passField2.clear();
                errorLabel.setText("");
            }
        });
    }

    private void loadUsers() {
        try {
            userTable.setItems(FXCollections.observableArrayList(apiService.getAllUsers()));
        } catch (Exception e) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Ошибка загрузки: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onCreateButtonClicked() {
        if (loginField.getText().isEmpty() || passField1.getText().isEmpty()) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Заполните логин и пароль");
            return;
        }
        
        if (!passField1.getText().equals(passField2.getText())) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Пароли не совпадают");
            return;
        }
        
        try {
            UserDTO newUser = new UserDTO();
            newUser.setLogin(loginField.getText());
            newUser.setPassword(passField1.getText());
            newUser.setName(loginField.getText());
            
            String role = roleChoiceBox.getValue();
            if (role == null) {
                errorLabel.setTextFill(Color.RED);
                errorLabel.setText("Выберите роль");
                return;
            }
            newUser.setRoleName(role);
            if ("administrator".equals(role)) newUser.setIdUserRole(1);
            else if ("organizer".equals(role)) newUser.setIdUserRole(2);
            else newUser.setIdUserRole(3);
            
            apiService.createUser(newUser);
            errorLabel.setTextFill(Color.GREEN);
            errorLabel.setText("Пользователь создан");
            clearFields();
            loadUsers();
        } catch (Exception e) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Ошибка создания: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onUpdateButtonClicked() {
        if (selectedUser == null) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Выберите пользователя из таблицы");
            return;
        }
        
        if (!passField1.getText().equals(passField2.getText())) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Пароли не совпадают");
            return;
        }
        
        try {
            selectedUser.setLogin(loginField.getText());
            if (!passField1.getText().isEmpty()) {
                selectedUser.setPassword(passField1.getText());
            }
            
            String role = roleChoiceBox.getValue();
            selectedUser.setRoleName(role);
            if ("administrator".equals(role)) selectedUser.setIdUserRole(1);
            else if ("organizer".equals(role)) selectedUser.setIdUserRole(2);
            else selectedUser.setIdUserRole(3);
            
            apiService.updateUser(selectedUser);
            errorLabel.setTextFill(Color.GREEN);
            errorLabel.setText("Данные обновлены");
            loadUsers();
        } catch (Exception e) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Ошибка обновления: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onDeleteButtonClicked() {
        if (selectedUser == null) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Выберите пользователя для удаления");
            return;
        }
        
        try {
            apiService.deleteUser(selectedUser.getIdUser());
            errorLabel.setTextFill(Color.GREEN);
            errorLabel.setText("Пользователь удалён");
            clearFields();
            loadUsers();
        } catch (Exception e) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Ошибка удаления: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() {
        try {
            App.setRoot("login");
        } catch (Exception e) {
            errorLabel.setText("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearFields() {
        loginField.clear();
        passField1.clear();
        passField2.clear();
        roleChoiceBox.setValue(null);
        selectedUser = null;
    }
}