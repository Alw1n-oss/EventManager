package ru.hse.eventmanager.controller;

import org.springframework.web.bind.annotation.*;
import ru.hse.eventmanager.dto.AuthRequest;
import ru.hse.eventmanager.dto.UserDTO;
import ru.hse.eventmanager.model.User;
import ru.hse.eventmanager.repository.UserRepository;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public UserDTO login(@RequestBody AuthRequest request) {
        Optional<User> userOpt = userRepository.findByLogin(request.getLogin());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(request.getPassword())) {
                UserDTO dto = new UserDTO();
                dto.setIdUser(user.getIdUser());
                dto.setLogin(user.getLogin());
                dto.setName(user.getName());
                if (user.getIdUserRole() != null) {
                    dto.setIdUserRole(user.getIdUserRole().getIdUserRole());
                    dto.setRoleName(user.getIdUserRole().getName());
                }
                return dto;
            }
        }
        throw new RuntimeException("Неверный логин или пароль");
    }
}