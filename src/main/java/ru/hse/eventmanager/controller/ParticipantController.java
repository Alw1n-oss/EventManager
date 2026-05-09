package ru.hse.eventmanager.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import ru.hse.eventmanager.App;
import ru.hse.eventmanager.model.Event;
import ru.hse.eventmanager.model.Registration;
import ru.hse.eventmanager.model.User;
import ru.hse.eventmanager.model.Venue;

import javax.persistence.*;
import java.text.SimpleDateFormat;

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

    private Event selectedEvent;
    private EntityManager em;
    private ObservableList<Event> allEvents = FXCollections.observableArrayList();
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    @FXML
    public void initialize() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ru.hse.eventmanager_PU");
        em = emf.createEntityManager();

        // Колонка Название
        catTitleColumn.setCellValueFactory(cell -> 
            new SimpleStringProperty(cell.getValue().getTitle()));

        // Колонка Дата — java.util.Date
        catDateColumn.setCellValueFactory(cell -> {
            java.util.Date d = cell.getValue().getEventDate();
            return new SimpleStringProperty(d != null ? sdf.format(d) : "");
        });

        // Колонка Место — Venue.name (getIdVenue() возвращает Venue)
        catVenueColumn.setCellValueFactory(cell -> {
            Venue v = cell.getValue().getIdVenue();
            return new SimpleStringProperty(v != null ? v.getName() : "");
        });

        loadEvents();

        catalogTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedEvent = newVal;
            if (newVal != null) {
                showEventDetails(newVal);
                errorLabel.setText("");
            } else {
                clearDetails();
            }
        });

        // Фильтрация при вводе текста
        filterField.textProperty().addListener((obs, oldVal, newVal) -> filterEvents(newVal));
    }

    private void loadEvents() {
        try {
            TypedQuery<Event> query = em.createQuery(
                "SELECT e FROM Event e WHERE e.status = 'ACTIVE' ORDER BY e.eventDate", Event.class);
            allEvents.setAll(query.getResultList());
            catalogTable.setItems(allEvents);
        } catch (Exception e) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Ошибка загрузки: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void filterEvents(String text) {
        if (text == null || text.trim().isEmpty()) {
            catalogTable.setItems(allEvents);
            return;
        }
        String lower = text.toLowerCase();
        ObservableList<Event> filtered = FXCollections.observableArrayList();
        for (Event e : allEvents) {
            if (e.getTitle() != null && e.getTitle().toLowerCase().contains(lower)) {
                filtered.add(e);
            }
        }
        catalogTable.setItems(filtered);
    }

    private void showEventDetails(Event e) {
        detailTitle.setText("Название: " + e.getTitle());
        java.util.Date d = e.getEventDate();
        detailDate.setText("Дата: " + (d != null ? sdf.format(d) : "-"));
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
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Выберите мероприятие из таблицы");
            return;
        }

        User current = App.getCurrentUser();
        if (current == null) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Ошибка: пользователь не авторизован");
            return;
        }

        if (selectedEvent.getCurrentParticipants() >= selectedEvent.getMaxParticipants()) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Мест больше нет");
            return;
        }

        try {
            // Проверка: не записан ли уже
            Long count = em.createQuery(
                "SELECT COUNT(r) FROM Registration r WHERE r.idEvent = :event AND r.idUser = :user", Long.class)
                .setParameter("event", selectedEvent)
                .setParameter("user", current)
                .getSingleResult();

            if (count > 0) {
                errorLabel.setTextFill(Color.RED);
                errorLabel.setText("Вы уже зарегистрированы на это мероприятие");
                return;
            }

            // Создание регистрации
            em.getTransaction().begin();

            Registration reg = new Registration();
            reg.setIdEvent(selectedEvent);
            reg.setIdUser(current);
            reg.setStatus("REGISTERED");
            reg.setRegisteredAt(new java.util.Date());
            em.persist(reg);

            selectedEvent.setCurrentParticipants(selectedEvent.getCurrentParticipants() + 1);
            em.merge(selectedEvent);

            em.getTransaction().commit();

            // Зелёная надпись внизу
            errorLabel.setTextFill(Color.GREEN);
            errorLabel.setText("Вы успешно зарегистрированы!");

            // ВСПЛЫВАЮЩЕЕ ОКНО — гарантированно видно
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Успешная регистрация");
            alert.setHeaderText(null);
            alert.setContentText("Вы записаны на мероприятие: " + selectedEvent.getTitle());
            alert.showAndWait();

            loadEvents();
            showEventDetails(selectedEvent);

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Ошибка регистрации: " + e.getMessage());
            e.printStackTrace();
        }
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