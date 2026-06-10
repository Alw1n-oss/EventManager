package ru.hse.eventmanager.service;

import org.springframework.stereotype.Service;
import ru.hse.eventmanager.dto.UserDTO;
import ru.hse.eventmanager.model.User;
import ru.hse.eventmanager.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO login(String login, String password) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Неверный логин или пароль"));

        // В учебной версии — прямое сравнение; в промышленной заменить на BCrypt.checkpw()
        if (!password.equals(user.getPassword())) {
            throw new RuntimeException("Неверный логин или пароль");
        }

        UserDTO dto = new UserDTO();
        dto.setIdUser(user.getIdUser());
        dto.setLogin(user.getLogin());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRoleName(user.getIdUserRole().getName());
        return dto;
    }
}