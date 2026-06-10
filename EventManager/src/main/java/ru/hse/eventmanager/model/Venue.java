package ru.hse.eventmanager.model;

public class Venue {
    private Integer idVenue;
    private String name;
    private String address;
    private Integer capacity;

    public Integer getIdVenue() { return idVenue; }
    public void setIdVenue(Integer idVenue) { this.idVenue = idVenue; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    @Override
    public String toString() {
        return name;
    }
}