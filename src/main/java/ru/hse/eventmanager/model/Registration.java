package ru.hse.eventmanager.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "Registration")
public class Registration implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idRegistration")
    private Integer idRegistration;

    @JoinColumn(name = "idUser", referencedColumnName = "idUser")
    @ManyToOne
    private User idUser;

    @JoinColumn(name = "idEvent", referencedColumnName = "idEvent")
    @ManyToOne
    private Event idEvent;

    @Column(name = "registeredAt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registeredAt;

    @Column(name = "status")
    private String status = "CONFIRMED";

    @Column(name = "isSynced")
    private Boolean isSynced = true;

    public Registration() {}

    public Integer getIdRegistration() { return idRegistration; }
    public void setIdRegistration(Integer idRegistration) { this.idRegistration = idRegistration; }

    public User getIdUser() { return idUser; }
    public void setIdUser(User idUser) { this.idUser = idUser; }

    public Event getIdEvent() { return idEvent; }
    public void setIdEvent(Event idEvent) { this.idEvent = idEvent; }

    public Date getRegisteredAt() { return registeredAt; }
    public void setRegisteredAt(Date registeredAt) { this.registeredAt = registeredAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Boolean getIsSynced() { return isSynced; }
    public void setIsSynced(Boolean isSynced) { this.isSynced = isSynced; }
}