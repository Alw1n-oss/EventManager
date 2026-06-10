package ru.hse.eventmanager.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import ru.hse.eventmanager.App;
import ru.hse.eventmanager.dto.EventDTO;
import ru.hse.eventmanager.dto.UserDTO;
import ru.hse.eventmanager.service.ApiService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ParticipantController {

    @FXML private TextField filterField;
    @FXML private TableView<EventDTO> catalogTable;
    @FXML private TableColumn<EventDTO, String> catTitleColumn;
    @FXML private TableColumn<EventDTO, String> catDateColumn;
    @FXML private TableColumn<EventDTO, String> catVenueColumn;
    @FXML private Label detailTitle;
    @FXML private Label detailDate;
    @FXML private Label detailDesc;
    @FXML private Label detailLimit;
    @FXML private Label errorLabel;

    private EventDTO selectedEvent;
    private final ApiService apiService = new ApiService();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    @FXML
    public void initialize() {
        catTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        catDateColumn.setCellValueFactory(cell -> {
            Date d = cell.getValue().getEventDate();
            return new SimpleStringProperty(d != null ? dateFormat.format(d) : "");
        });
        catVenueColumn.setCellValueFactory(new PropertyValueFactory<>("venueName"));
        
        loadEvents();
        
        catalogTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedEvent = newVal;
                detailTitle.setText("Название: " + newVal.getTitle());
                detailDate.setText("Дата: " + (newVal.getEventDate() != null ? dateFormat.format(newVal.getEventDate()) : ""));
                detailDesc.setText("Описание: " + (newVal.getDescription() != null ? newVal.getDescription() : ""));
                int free = newVal.getMaxParticipants() - newVal.getCurrentParticipants();
                detailLimit.setText("Осталось мест: " + free + " / " + newVal.getMaxParticipants());
                detailLimit.setTextFill(free > 0 ? Color.web("#0066cc") : Color.RED);
            }
        });
        
        filterField.textProperty().addListener((obs, oldVal, newVal) -> filterEvents(newVal));
    }

    private void loadEvents() {
        try {
            catalogTable.setItems(FXCollections.observableArrayList(apiService.getAllEvents()));
        } catch (Exception e) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Ошибка загрузки: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void filterEvents(String query) {
        if (query == null || query.isEmpty()) {
            loadEvents();
            return;
        }
        String lower = query.toLowerCase();
        var filtered = catalogTable.getItems().stream()
            .filter(e -> e.getTitle().toLowerCase().contains(lower))
            .toList();
        catalogTable.setItems(FXCollections.observableArrayList(filtered));
    }

    @FXML
    private void onRegisterClicked() {
        if (selectedEvent == null) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Выберите мероприятие из таблицы");
            return;
        }
        if (selectedEvent.getCurrentParticipants() >= selectedEvent.getMaxParticipants()) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Мест больше нет");
            return;
        }
        
        try {
            UserDTO current = ApiService.getCurrentUser();
            if (current == null) {
                errorLabel.setTextFill(Color.RED);
                errorLabel.setText("Ошибка: пользователь не авторизован");
                return;
            }
            
            String result = apiService.registerForEvent(current.getIdUser(), selectedEvent.getIdEvent());
            errorLabel.setTextFill(Color.GREEN);
            errorLabel.setText(result);
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Успешная регистрация");
            alert.setHeaderText(null);
            alert.setContentText("Вы записаны на: " + selectedEvent.getTitle());
            alert.showAndWait();
            
            loadEvents();
        } catch (Exception e) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Ошибка регистрации: " + e.getMessage());
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
}