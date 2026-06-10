package ru.hse.eventmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hse.eventmanager.model.Event;

public interface EventRepository extends JpaRepository<Event, Integer> {
}