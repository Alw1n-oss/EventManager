package ru.hse.eventmanager.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.hse.eventmanager.App;
import ru.hse.eventmanager.model.Event;

import java.util.Date;

public class ParticipantController {

    @FXML private TextField filterField;
    @FXML private TableView<Event> catalogTable;
    @FXML private TableColumn<Event, String> catTitleColumn;
    @FXML private TableColumn<Event, String> catDateColumn;
    @FXML private TableColumn<Event, String> catVenueColumn;
    @FXML private Label detailTitle;
    @FXML private Label detailDate;
    @FXML private Label detailDesc;
    @FXML private Label detailLimit;
    @FXML private Label errorLabel;

    private ObservableList<Event> eventList = FXCollections.observableArrayList();
    private Event selectedEvent;

    @FXML
    public void initialize() {
        System.out.println("DEBUG: ParticipantController.initialize() вызван");

        // Привязка колонок
        catTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        catDateColumn.setCellValueFactory(new PropertyValueFactory<>("eventDate"));
        catVenueColumn.setCellValueFactory(new PropertyValueFactory<>("venue"));

        // Тестовые данные
        eventList.add(new Event(1, "Встреча клуба Java", "Обсуждение новых фич Java 21", 
                new Date(126, 4, 15, 18, 0), "Аудитория 101", 30, 12));
        eventList.add(new Event(2, "Мастер-класс по UX", "Практикум по проектированию интерфейсов", 
                new Date(126, 4, 20, 14, 0), "Коворкинг «Точка»", 20, 5));
        eventList.add(new Event(3, "Городской хакатон", "24-часовой марафон программирования", 
                new Date(126, 5, 1, 10, 0), "IT-парк", 50, 48));

        catalogTable.setItems(eventList);

        // При клике на строку — показать детали
        catalogTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedEvent = newVal;
            if (newVal != null) {
                showEventDetails(newVal);
                errorLabel.setText("");
            } else {
                clearDetails();
            }
        });
    }

    private void showEventDetails(Event e) {
        detailTitle.setText("Название: " + e.getTitle());
        detailDate.setText("Дата: " + e.getEventDate());
        detailDesc.setText("Описание: " + (e.getDescription() != null ? e.getDescription() : "-"));
        int left = e.getMaxParticipants() - e.getCurrentParticipants();
        detailLimit.setText("Осталось мест: " + left + " / " + e.getMaxParticipants());
    }

    private void clearDetails() {
        detailTitle.setText("Название: ");
        detailDate.setText("Дата: ");
        detailDesc.setText("Описание: ");
        detailLimit.setText("Осталось мест: ");
    }

    @FXML
    private void onRegisterClicked() {
        if (selectedEvent == null) {
            errorLabel.setText("Выберите мероприятие из таблицы");
            return;
        }
        
        int left = selectedEvent.getMaxParticipants() - selectedEvent.getCurrentParticipants();
        if (left <= 0) {
            showAlert("Ошибка", "Мероприятие заполнено!");
            return;
        }

        // Увеличиваем счётчик участников (пока без БД)
        selectedEvent.setCurrentParticipants(selectedEvent.getCurrentParticipants() + 1);
        
        // Обновляем отображение
        showEventDetails(selectedEvent);
        catalogTable.refresh();
        
        showAlert("Успех", "Вы зарегистрированы на: " + selectedEvent.getTitle());
        errorLabel.setText("");
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    private void handleBack() {
        try {
            App.setRoot("login");
        } catch (Exception e) {
            if (errorLabel != null) errorLabel.setText("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}