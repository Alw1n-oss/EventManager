package ru.hse.eventmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hse.eventmanager.model.Event;
import ru.hse.eventmanager.model.Registration;
import ru.hse.eventmanager.model.User;

import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration, Integer> {
    List<Registration> findByIdUserAndIdEvent(User user, Event event);
    List<Registration> findByIdEvent(Event event);
}