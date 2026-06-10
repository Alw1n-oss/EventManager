package ru.hse.eventmanager.model;

import java.sql.Timestamp;

public class Registration {
    private Integer idRegistration;
    private User idUser;
    private Event idEvent;
    private Timestamp registeredAt;
    private String status = "CONFIRMED";
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