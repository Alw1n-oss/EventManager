package ru.hse.eventmanager.model;

public class User {
    private Integer idUser;
    private String login;
    private String password;
    private String name;
    private String email;
    private UserRole idUserRole;

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