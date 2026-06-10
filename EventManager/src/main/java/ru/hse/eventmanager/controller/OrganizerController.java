package ru.hse.eventmanager.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import ru.hse.eventmanager.App;
import ru.hse.eventmanager.dto.EventDTO;
import ru.hse.eventmanager.dto.VenueDTO;
import ru.hse.eventmanager.service.ApiService;

import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.stage.FileChooser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.FileOutputStream;

public class OrganizerController {

    @FXML private TableView<EventDTO> eventTable;
    @FXML private TableColumn<EventDTO, String> titleColumn;
    @FXML private TableColumn<EventDTO, String> dateColumn;
    @FXML private TableColumn<EventDTO, String> venueColumn;
    @FXML private TableColumn<EventDTO, String> limitColumn;
    
    @FXML private TextField titleField;
    @FXML private DatePicker datePicker;
    @FXML private ChoiceBox<VenueDTO> venueChoiceBox;
    @FXML private TextField limitField;
    @FXML private TextArea descText;
    @FXML private Label errorLabel;

    private final ApiService apiService = new ApiService();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        dateColumn.setCellValueFactory(cell -> {
            Date d = cell.getValue().getEventDate();
            return new SimpleStringProperty(d != null ? dateFormat.format(d) : "");
        });
        venueColumn.setCellValueFactory(new PropertyValueFactory<>("venueName"));
        limitColumn.setCellValueFactory(cell -> {
            EventDTO e = cell.getValue();
            return new SimpleStringProperty(e.getCurrentParticipants() + " / " + e.getMaxParticipants());
        });
        
        loadVenues();
        loadEvents();
        
        eventTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                titleField.setText(newVal.getTitle());
                if (newVal.getEventDate() != null) {
                    datePicker.setValue(newVal.getEventDate().toInstant()
                        .atZone(java.time.ZoneId.systemDefault()).toLocalDate());
                }
                for (VenueDTO v : venueChoiceBox.getItems()) {
                    if (v.getIdVenue().equals(newVal.getVenueId())) {
                        venueChoiceBox.setValue(v);
                        break;
                    }
                }
                limitField.setText(String.valueOf(newVal.getMaxParticipants()));
                descText.setText(newVal.getDescription());
            }
        });
    }

    private void loadVenues() {
        try {
            venueChoiceBox.setItems(FXCollections.observableArrayList(apiService.getAllVenues()));
            venueChoiceBox.setConverter(new StringConverter<VenueDTO>() {
                @Override public String toString(VenueDTO v) { return v != null ? v.getName() : ""; }
                @Override public VenueDTO fromString(String s) { return null; }
            });
        } catch (Exception e) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Ошибка загрузки мест: " + e.getMessage());
        }
    }

    private void loadEvents() {
        try {
            eventTable.setItems(FXCollections.observableArrayList(apiService.getAllEvents()));
        } catch (Exception e) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Ошибка загрузки: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onCreateEvent() {
        if (titleField.getText().isEmpty() || datePicker.getValue() == null || 
            venueChoiceBox.getValue() == null || limitField.getText().isEmpty()) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Заполните все обязательные поля");
            return;
        }
        
        try {
            EventDTO event = new EventDTO();
            event.setTitle(titleField.getText());
            event.setEventDate(java.sql.Date.valueOf(datePicker.getValue()));
            event.setVenueId(venueChoiceBox.getValue().getIdVenue());
            event.setMaxParticipants(Integer.parseInt(limitField.getText()));
            event.setCurrentParticipants(0);
            event.setDescription(descText.getText());
            event.setStatus("ACTIVE");
            event.setOrganizerId(ApiService.getCurrentUser().getIdUser());
            
            apiService.createEvent(event);
            errorLabel.setTextFill(Color.GREEN);
            errorLabel.setText("Мероприятие создано");
            clearFields();
            loadEvents();
        } catch (NumberFormatException e) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Лимит должен быть числом");
        } catch (Exception e) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Ошибка создания: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onUpdateEvent() {
        EventDTO selected = eventTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Выберите мероприятие для изменения");
            return;
        }
        try {
            selected.setTitle(titleField.getText());
            selected.setEventDate(java.sql.Date.valueOf(datePicker.getValue()));
            selected.setVenueId(venueChoiceBox.getValue().getIdVenue());
            selected.setMaxParticipants(Integer.parseInt(limitField.getText()));
            selected.setDescription(descText.getText());
            
            apiService.updateEvent(selected);
            errorLabel.setTextFill(Color.GREEN);
            errorLabel.setText("Мероприятие обновлено");
            loadEvents();
        } catch (Exception e) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Ошибка обновления: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onDeleteEvent() {
        EventDTO selected = eventTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Выберите мероприятие для удаления");
            return;
        }
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Подтверждение удаления");
        confirm.setHeaderText(null);
        confirm.setContentText("Удалить мероприятие \"" + selected.getTitle() + "\"?");
        if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }
        
        try {
            apiService.deleteEvent(selected.getId());
            errorLabel.setTextFill(Color.GREEN);
            errorLabel.setText("Мероприятие удалено");
            clearFields();
            loadEvents();
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
    
    @FXML
    private void onDownloadReport() {
        EventDTO selected = eventTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            errorLabel.setText("Выберите мероприятие из таблицы");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить отчёт PDF");
        fileChooser.setInitialFileName("report_event_" + selected.getId() + ".pdf");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        java.io.File file = fileChooser.showSaveDialog(eventTable.getScene().getWindow());

        if (file == null) return;

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/events/" + selected.getId() + "/report"))
                    .GET()
                    .build();

            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

            if (response.statusCode() == 200) {
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(response.body());
                }
                errorLabel.setTextFill(javafx.scene.paint.Color.GREEN);
                errorLabel.setText("Отчёт сохранён: " + file.getName());
            } else {
                errorLabel.setText("Ошибка сервера: " + response.statusCode());
            }
        } catch (Exception e) {
            errorLabel.setText("Ошибка загрузки: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearFields() {
        titleField.clear();
        datePicker.setValue(null);
        venueChoiceBox.setValue(null);
        limitField.clear();
        descText.clear();
    }
}