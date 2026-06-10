package ru.hse.eventmanager.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.eventmanager.model.Event;
import ru.hse.eventmanager.model.Registration;
import ru.hse.eventmanager.model.User;
import ru.hse.eventmanager.repository.EventRepository;
import ru.hse.eventmanager.repository.RegistrationRepository;
import ru.hse.eventmanager.repository.UserRepository;

import java.sql.Timestamp;
import java.util.Date;

import java.util.List;

@Service
public class RegistrationService {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    private final UserRepository userRepository;

    public RegistrationService(EventRepository eventRepository,
                                 RegistrationRepository registrationRepository,
                                 UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
        this.userRepository = userRepository;
    }

        @Transactional
    public void register(Integer userId, Integer eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Мероприятие не найдено"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

                // Проверка: не записан ли уже
        List<Registration> existing = registrationRepository.findByIdUserAndIdEvent(user, event);
        if (!existing.isEmpty()) {
            throw new RuntimeException("Вы уже зарегистрированы");
        }

        // Проверка лимита
        if (event.getCurrentParticipants() >= event.getMaxParticipants()) {
            throw new RuntimeException("Мероприятие заполнено");
        }

        // Создание регистрации
        Registration reg = new Registration();
        reg.setIdEvent(event);
        reg.setIdUser(user);
        reg.setStatus("CONFIRMED");
        reg.setRegisteredAt(new Timestamp(new Date().getTime()));
        registrationRepository.save(reg);

        // Инкремент счётчика
        event.setCurrentParticipants(event.getCurrentParticipants() + 1);
        eventRepository.save(event);
    }
}