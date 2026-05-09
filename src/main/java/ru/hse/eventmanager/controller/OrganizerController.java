package ru.hse.eventmanager.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import ru.hse.eventmanager.App;
import ru.hse.eventmanager.model.Event;
import ru.hse.eventmanager.model.User;
import ru.hse.eventmanager.model.Venue;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class OrganizerController {

    @FXML private TableView<Event> eventTable;
    @FXML private TableColumn<Event, String> titleColumn;
    @FXML private TableColumn<Event, String> dateColumn;
    @FXML private TableColumn<Event, String> venueColumn;
    @FXML private TableColumn<Event, String> limitColumn;
    
    @FXML private TextField titleField;
    @FXML private DatePicker datePicker;
    @FXML private ChoiceBox<Venue> venueChoiceBox;
    @FXML private TextField limitField;
    @FXML private TextArea descText;
    @FXML private Label errorLabel;

    private EntityManager em;
    private Event selectedEvent;
    private ObservableList<Event> events = FXCollections.observableArrayList();
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    @FXML
    public void initialize() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ru.hse.eventmanager_PU");
        em = emf.createEntityManager();

        titleColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTitle()));
        dateColumn.setCellValueFactory(cell -> {
            Date d = cell.getValue().getEventDate();
            return new SimpleStringProperty(d != null ? sdf.format(d) : "");
        });
        venueColumn.setCellValueFactory(cell -> {
            Venue v = cell.getValue().getIdVenue();
            return new SimpleStringProperty(v != null ? v.getName() : "");
        });
        limitColumn.setCellValueFactory(cell -> 
            new SimpleStringProperty(String.valueOf(cell.getValue().getMaxParticipants())));

        loadVenues();
        loadEvents();

        eventTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedEvent = newVal;
            if (newVal != null) fillFields(newVal);
        });
    }

    private void loadVenues() {
        TypedQuery<Venue> query = em.createQuery("SELECT v FROM Venue v", Venue.class);
        venueChoiceBox.getItems().setAll(query.getResultList());
    }

    private void loadEvents() {
        User current = App.getCurrentUser();
        TypedQuery<Event> query = em.createQuery(
            "SELECT e FROM Event e WHERE e.idOrganizer = :org ORDER BY e.eventDate", Event.class);
        query.setParameter("org", current);
        events.setAll(query.getResultList());
        eventTable.setItems(events);
    }

    private void fillFields(Event e) {
        titleField.setText(e.getTitle());
        descText.setText(e.getDescription());
        limitField.setText(String.valueOf(e.getMaxParticipants()));
        venueChoiceBox.setValue(e.getIdVenue());
        if (e.getEventDate() != null) {
            LocalDate ld = e.getEventDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            datePicker.setValue(ld);
        }
    }

    private void clearFields() {
        titleField.clear();
        descText.clear();
        limitField.clear();
        datePicker.setValue(null);
        venueChoiceBox.setValue(null);
        selectedEvent = null;
        eventTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void onCreateEvent() {
        if (!validate()) return;

        try {
            em.getTransaction().begin();

            Event e = new Event();
            e.setTitle(titleField.getText().trim());
            e.setDescription(descText.getText().trim());
            e.setMaxParticipants(Integer.parseInt(limitField.getText().trim()));
            e.setIdVenue(venueChoiceBox.getValue());
            e.setCurrentParticipants(0);
            e.setStatus("ACTIVE");
            e.setIdOrganizer(App.getCurrentUser());

            LocalDate ld = datePicker.getValue();
            Date date = Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
            e.setEventDate(date);

            em.persist(e);
            em.getTransaction().commit();

            errorLabel.setTextFill(Color.GREEN);
            errorLabel.setText("Мероприятие создано");
            clearFields();
            loadEvents();

        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Ошибка создания: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void onUpdateEvent() {
        if (selectedEvent == null) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Выберите мероприятие для изменения");
            return;
        }
        if (!validate()) return;

        try {
            em.getTransaction().begin();

            selectedEvent.setTitle(titleField.getText().trim());
            selectedEvent.setDescription(descText.getText().trim());
            selectedEvent.setMaxParticipants(Integer.parseInt(limitField.getText().trim()));
            selectedEvent.setIdVenue(venueChoiceBox.getValue());

            LocalDate ld = datePicker.getValue();
            Date date = Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
            selectedEvent.setEventDate(date);

            em.merge(selectedEvent);
            em.getTransaction().commit();

            errorLabel.setTextFill(Color.GREEN);
            errorLabel.setText("Мероприятие обновлено");
            clearFields();
            loadEvents();

        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Ошибка обновления: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private boolean validate() {
        if (titleField.getText().trim().isEmpty()) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Введите название");
            return false;
        }
        if (datePicker.getValue() == null) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Выберите дату");
            return false;
        }
        if (venueChoiceBox.getValue() == null) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Выберите место");
            return false;
        }
        try {
            Integer.parseInt(limitField.getText().trim());
        } catch (NumberFormatException e) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Лимит должен быть числом");
            return false;
        }
        return true;
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