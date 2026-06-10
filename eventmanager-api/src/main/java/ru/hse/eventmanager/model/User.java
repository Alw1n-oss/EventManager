package ru.hse.eventmanager.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUser")
    private Integer idUser;

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @ManyToOne
    @JoinColumn(name = "idUserRole", nullable = false)
    private UserRole idUserRole;

    @OneToMany(mappedBy = "idUser", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Registration> registrations;

    @OneToMany(mappedBy = "idOrganizer", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Event> organizedEvents;

    public Integer getIdUser() { return idUser; }
    public void setIdUser(Integer idUser) { this.idUser = idUser; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public UserRole getIdUserRole() { return idUserRole; }
    public void setIdUserRole(UserRole idUserRole) { this.idUserRole = idUserRole; }
}