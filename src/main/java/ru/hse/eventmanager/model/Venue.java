package ru.hse.eventmanager.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Venue")
@NamedQueries({
    @NamedQuery(name = "Venue.findAll", query = "SELECT v FROM Venue v")
})
public class Venue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idVenue")
    private Integer idVenue;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "capacity")
    private Integer capacity;

    public Venue() {}

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