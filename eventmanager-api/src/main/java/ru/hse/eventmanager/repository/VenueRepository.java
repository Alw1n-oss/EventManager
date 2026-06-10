package ru.hse.eventmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hse.eventmanager.model.Venue;

public interface VenueRepository extends JpaRepository<Venue, Integer> {
}