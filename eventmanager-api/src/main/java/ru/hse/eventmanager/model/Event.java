package ru.hse.eventmanager.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEvent")
    private Integer idEvent;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "eventDate", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventDate;

    @ManyToOne
    @JoinColumn(name = "idVenue")
    private Venue idVenue;

    @Column(name = "maxParticipants", nullable = false)
    private Integer maxParticipants;

    @Column(name = "currentParticipants")
    private Integer currentParticipants = 0;

    @Column(name = "status")
    private String status = "ACTIVE";

    @ManyToOne
    @JoinColumn(name = "idOrganizer", nullable = false)
    private User idOrganizer;

    @Version
    @Column(name = "version")
    private Long version = 0L;

    @OneToMany(mappedBy = "idEvent", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Registration> registrations;

    // ... геттеры/сеттеры


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