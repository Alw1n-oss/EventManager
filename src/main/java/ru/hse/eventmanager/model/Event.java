package ru.hse.eventmanager.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "Event")
@NamedQueries({
    @NamedQuery(name = "Event.findAll", query = "SELECT e FROM Event e"),
    @NamedQuery(name = "Event.findByTitle", query = "SELECT e FROM Event e WHERE e.title = :title")
})
public class Event implements Serializable {

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

    @JoinColumn(name = "idVenue", referencedColumnName = "idVenue")
    @ManyToOne
    private Venue idVenue;

    @Column(name = "maxParticipants", nullable = false)
    private Integer maxParticipants;

    @Column(name = "currentParticipants")
    private Integer currentParticipants = 0;

    @Column(name = "status")
    private String status = "ACTIVE";

    @JoinColumn(name = "idOrganizer", referencedColumnName = "idUser")
    @ManyToOne
    private User idOrganizer;

    @Version
    @Column(name = "version")
    private Long version = 0L;

    public Event() {}

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