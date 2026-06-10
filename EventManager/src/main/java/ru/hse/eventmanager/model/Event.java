package ru.hse.eventmanager.model;

import java.util.Date;

public class Event {
    private Integer idEvent;
    private String title;
    private String description;
    private Date eventDate;
    private Venue idVenue;
    private Integer maxParticipants;
    private Integer currentParticipants = 0;
    private String status = "ACTIVE";
    private User idOrganizer;
    private Long version = 0L;

    public Integer getIdEvent() { return idEvent; }
    public void setIdEvent(Integer idEvent) { this.idEvent = idEvent; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Date getEventDate() { return eventDate; }
    public void setEventDate(Date eventDate) { this.eventDate = eventDate; }
    public Venue getIdVenue() { return idVenue; }
    public void setIdVenue(Venue idVenue) { this.idVenue = idVenue; }
    public Integer getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(Integer maxParticipants) { this.maxParticipants = maxParticipants; }
    public Integer getCurrentParticipants() { return currentParticipants; }
    public void setCurrentParticipants(Integer currentParticipants) { this.currentParticipants = currentParticipants; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public User getIdOrganizer() { return idOrganizer; }
    public void setIdOrganizer(User idOrganizer) { this.idOrganizer = idOrganizer; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}