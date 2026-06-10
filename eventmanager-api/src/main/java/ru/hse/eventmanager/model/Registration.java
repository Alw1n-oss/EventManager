package ru.hse.eventmanager.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "Registration")
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idRegistration")
    private Integer idRegistration;

    @ManyToOne
    @JoinColumn(name = "idUser", nullable = false)
    private User idUser;

    @ManyToOne
    @JoinColumn(name = "idEvent", nullable = false)
    private Event idEvent;

    @Column(name = "registeredAt")
    private Timestamp registeredAt;

    @Column(name = "status")
    private String status = "CONFIRMED";

    @Column(name = "isSynced")
    private Boolean isSynced = true;

    public Integer getIdRegistration() { return idRegistration; }
    public void setIdRegistration(Integer idRegistration) { this.idRegistration = idRegistration; }
    public User getIdUser() { return idUser; }
    public void setIdUser(User idUser) { this.idUser = idUser; }
    public Event getIdEvent() { return idEvent; }
    public void setIdEvent(Event idEvent) { this.idEvent = idEvent; }
    public Timestamp getRegisteredAt() { return registeredAt; }
    public void setRegisteredAt(Timestamp registeredAt) { this.registeredAt = registeredAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Boolean getIsSynced() { return isSynced; }
    public void setIsSynced(Boolean isSynced) { this.isSynced = isSynced; }
}