package ru.hse.eventmanager.model;

import java.util.Date;

public class Event {
    private Integer idEvent;
    private String title;
    private String description;
    private Date eventDate;
    private String venue;      // пока просто строка, потом заменим на объект Venue
    private Integer maxParticipants;
    private Integer currentParticipants;

    public Event() {}

    public Event(Integer idEvent, String title, String description, Date eventDate, 
                 String venue, Integer maxParticipants, Integer currentParticipants) {
        this.idEvent = idEvent;
        this.title = title;
        this.description = description;
        this.eventDate = eventDate;
        this.venue = venue;
        this.maxParticipants = maxParticipants;
        this.currentParticipants = currentParticipants;
    }

    public Integer getIdEvent() { return idEvent; }
    public void setIdEvent(Integer idEvent) { this.idEvent = idEvent; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getEventDate() { return eventDate; }
    public void setEventDate(Date eventDate) { this.eventDate = eventDate; }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }

    public Integer getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(Integer maxParticipants) { this.maxParticipants = maxParticipants; }

    public Integer getCurrentParticipants() { return currentParticipants; }
    public void setCurrentParticipants(Integer currentParticipants) { this.currentParticipants = currentParticipants; }
}
