package ru.hse.eventmanager.controller;

import org.springframework.web.bind.annotation.*;
import ru.hse.eventmanager.model.Event;
import ru.hse.eventmanager.model.Registration;
import ru.hse.eventmanager.model.User;
import ru.hse.eventmanager.repository.EventRepository;
import ru.hse.eventmanager.repository.RegistrationRepository;
import ru.hse.eventmanager.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.sql.Timestamp;
import java.util.Optional;

@RestController
@RequestMapping("/api/registrations")
@CrossOrigin(origins = "*")
public class RegistrationController {

    private final RegistrationRepository registrationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public RegistrationController(RegistrationRepository registrationRepository, 
                                  EventRepository eventRepository, 
                                  UserRepository userRepository) {
        this.registrationRepository = registrationRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public String register(@RequestBody Map<String, Integer> request) {
        Integer userId = request.get("idUser");
        Integer eventId = request.get("idEvent");

        Optional<Event> eventOpt = eventRepository.findById(eventId);
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (eventOpt.isEmpty() || userOpt.isEmpty()) {
            return "Ошибка: пользователь или мероприятие не найдены";
        }
        
        Event event = eventOpt.get();
        User user = userOpt.get();
        
        // Проверка: уже зарегистрирован?
        List<Registration> existing = registrationRepository.findByIdUserAndIdEvent(user, event);
        if (!existing.isEmpty()) {
            return "Вы уже зарегистрированы на это мероприятие";
        }
        
        if (event.getCurrentParticipants() >= event.getMaxParticipants()) {
            return "Ошибка: мест нет";
        }
        
        Registration reg = new Registration();
        reg.setIdUser(user);
        reg.setIdEvent(event);
        reg.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
        reg.setStatus("CONFIRMED");
        registrationRepository.save(reg);
        
        event.setCurrentParticipants(event.getCurrentParticipants() + 1);
        eventRepository.save(event);
        
        return "Успешно зарегистрированы";
    }
}