package ru.hse.eventmanager.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.hse.eventmanager.App;

public class OrganizerController {

    @FXML
    private TableView<EventView> eventTable;
    @FXML
    private TableColumn<EventView, String> titleColumn;
    @FXML
    private TableColumn<EventView, String> dateColumn;
    @FXML
    private TableColumn<EventView, String> venueColumn;
    @FXML
    private TableColumn<EventView, String> limitColumn;
    
    @FXML
    private TextField titleField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ChoiceBox<String> venueChoiceBox;
    @FXML
    private TextField limitField;
    @FXML
    private TextArea descText;
    @FXML
    private Label errorLabel;

    public static class EventView {
        private String title, date, venue, limit;
        public EventView(String title, String date, String venue, String limit) {
            this.title = title; this.date = date; this.venue = venue; this.limit = limit;
        }
        public String getTitle() { return title; }
        public String getDate() { return date; }
        public String getVenue() { return venue; }
        public String getLimit() { return limit; }
    }

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        venueColumn.setCellValueFactory(new PropertyValueFactory<>("venue"));
        limitColumn.setCellValueFactory(new PropertyValueFactory<>("limit"));
        
        ObservableList<EventView> events = FXCollections.observableArrayList(
            new EventView("Встреча клуба", "2026-05-15", "Аудитория 101", "30"),
            new EventView("Мастер-класс", "2026-05-20", "Конференц-зал", "50")
        );
        eventTable.setItems(events);
        
        venueChoiceBox.getItems().addAll("Аудитория 101", "Конференц-зал", "Коворкинг");
    }

    @FXML
    private void onCreateEvent() {
        errorLabel.setText("Создание мероприятия (без БД)");
    }

    @FXML
    private void onUpdateEvent() {
        errorLabel.setText("Изменение мероприятия (без БД)");
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
