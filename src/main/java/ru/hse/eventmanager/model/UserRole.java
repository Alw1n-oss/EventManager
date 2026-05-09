package ru.hse.eventmanager.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "UserRole")
@NamedQueries({
    @NamedQuery(name = "UserRole.findAll", query = "SELECT r FROM UserRole r")
})
public class UserRole implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUserRole")
    private Integer idUserRole;

    @Column(name = "name", nullable = false)
    private String name;

    public Integer getIdUserRole() {
        return idUserRole;
    }

    public void setIdUserRole(Integer idUserRole) {
        this.idUserRole = idUserRole;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
