package ru.hse.eventmanager.controller;

import org.springframework.web.bind.annotation.*;
import ru.hse.eventmanager.dto.UserDTO;
import ru.hse.eventmanager.model.User;
import ru.hse.eventmanager.model.UserRole;
import ru.hse.eventmanager.repository.UserRepository;
import ru.hse.eventmanager.repository.UserRoleRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public UserController(UserRepository userRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable Integer id) {
        return userRepository.findById(id).map(this::toDTO).orElse(null);
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable Integer id, @RequestBody UserDTO dto) {
        User user = userRepository.findById(id).orElseThrow();
        user.setLogin(dto.getLogin());
        user.setName(dto.getName());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(dto.getPassword());
        }
        if (dto.getIdUserRole() != null) {
            UserRole role = userRoleRepository.findById(dto.getIdUserRole()).orElse(null);
            user.setIdUserRole(role);
        }
        return toDTO(userRepository.save(user));
    }

    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO dto) {
        User user = new User();
        user.setLogin(dto.getLogin());
        user.setPassword(dto.getPassword());
        user.setName(dto.getName());
        if (dto.getIdUserRole() != null) {
            UserRole role = userRoleRepository.findById(dto.getIdUserRole()).orElse(null);
            user.setIdUserRole(role);
        }
        User saved = userRepository.save(user);
        return toDTO(saved);
    }

    private UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setIdUser(user.getIdUser());
        dto.setLogin(user.getLogin());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        if (user.getIdUserRole() != null) {
            dto.setIdUserRole(user.getIdUserRole().getIdUserRole());
            dto.setRoleName(user.getIdUserRole().getName());
        }
        return dto;
    }
    
    @DeleteMapping("/{id}")
        public void deleteUser(@PathVariable Integer id) {
        userRepository.deleteById(id);
    }
}