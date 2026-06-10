package ru.hse.eventmanager.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventDTO {
    private Integer id;           // сервер шлёт "id"
    private String title;
    private String description;
    private Date eventDate;
    private Integer venueId;      // сервер шлёт "venueId"
    private String venueName;
    private Integer maxParticipants;
    private Integer currentParticipants;
    private String status;
    private Integer organizerId;  // сервер шлёт "organizerId"
    private String organizerName;

    public EventDTO() {}

    // Геттеры/сеттеры под формат сервера
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Date getEventDate() { return eventDate; }
    public void setEventDate(Date eventDate) { this.eventDate = eventDate; }
    
    public Integer getVenueId() { return venueId; }
    public void setVenueId(Integer venueId) { this.venueId = venueId; }
    
    public String getVenueName() { return venueName; }
    public void setVenueName(String venueName) { this.venueName = venueName; }
    
    public Integer getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(Integer maxParticipants) { this.maxParticipants = maxParticipants; }
    
    public Integer getCurrentParticipants() { return currentParticipants; }
    public void setCurrentParticipants(Integer currentParticipants) { this.currentParticipants = currentParticipants; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Integer getOrganizerId() { return organizerId; }
    public void setOrganizerId(Integer organizerId) { this.organizerId = organizerId; }
    
    public String getOrganizerName() { return organizerName; }
    public void setOrganizerName(String organizerName) { this.organizerName = organizerName; }
    
    // Обратная совместимость (чтобы не ломать контроллеры)
    public Integer getIdEvent() { return id; }
    public Integer getIdVenue() { return venueId; }
    public Integer getIdOrganizer() { return organizerId; }
}