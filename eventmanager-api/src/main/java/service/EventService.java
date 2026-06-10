package ru.hse.eventmanager.service;

import org.springframework.stereotype.Service;
import ru.hse.eventmanager.dto.EventDTO;
import ru.hse.eventmanager.model.Event;
import ru.hse.eventmanager.model.Registration;
import ru.hse.eventmanager.model.User;
import ru.hse.eventmanager.model.Venue;
import ru.hse.eventmanager.repository.EventRepository;
import ru.hse.eventmanager.repository.RegistrationRepository;
import ru.hse.eventmanager.repository.UserRepository;
import ru.hse.eventmanager.repository.VenueRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;
    private final UserRepository userRepository;
    private final RegistrationRepository registrationRepository;

    public EventService(EventRepository eventRepository,
                        VenueRepository venueRepository,
                        UserRepository userRepository,
                        RegistrationRepository registrationRepository) {
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
        this.userRepository = userRepository;
        this.registrationRepository = registrationRepository;
    }

    public List<EventDTO> findAllActive() {
        return eventRepository.findAll().stream()
            .filter(e -> "ACTIVE".equals(e.getStatus()))
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public List<EventDTO> findAll() {
        return eventRepository.findAll().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public EventDTO findById(Integer id) {
        return eventRepository.findById(id)
            .map(this::toDTO)
            .orElseThrow(() -> new RuntimeException("Мероприятие не найдено"));
    }

    public EventDTO create(EventDTO dto) {
        Event event = new Event();
        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setEventDate(dto.getEventDate());
        event.setMaxParticipants(dto.getMaxParticipants());
        event.setCurrentParticipants(0);
        event.setStatus("ACTIVE");

        if (dto.getVenueId() != null) {
            Venue venue = venueRepository.findById(dto.getVenueId()).orElse(null);
            event.setIdVenue(venue);
        }

        if (dto.getOrganizerId() != null) {
            User organizer = userRepository.findById(dto.getOrganizerId()).orElse(null);
            event.setIdOrganizer(organizer);
        }

        return toDTO(eventRepository.save(event));
    }

    public EventDTO update(Integer id, EventDTO dto) {
        Event event = eventRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Мероприятие не найдено"));

        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setEventDate(dto.getEventDate());
        event.setMaxParticipants(dto.getMaxParticipants());
        event.setStatus(dto.getStatus());

        if (dto.getVenueId() != null) {
            Venue venue = venueRepository.findById(dto.getVenueId()).orElse(null);
            event.setIdVenue(venue);
        }

        if (dto.getOrganizerId() != null) {
            User organizer = userRepository.findById(dto.getOrganizerId()).orElse(null);
            event.setIdOrganizer(organizer);
        }

        return toDTO(eventRepository.save(event));
    }

    public void delete(Integer id) {
        Event event = eventRepository.findById(id).orElse(null);
        if (event != null) {
            List<Registration> regs = registrationRepository.findByIdEvent(event);
            registrationRepository.deleteAll(regs);
        }
        eventRepository.deleteById(id);
    }

    public void register(Integer userId, Integer eventId) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Мероприятие не найдено"));
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (event.getCurrentParticipants() >= event.getMaxParticipants()) {
            throw new RuntimeException("Мест нет");
        }

        List<Registration> existing = registrationRepository.findByIdUserAndIdEvent(user, event);
        if (!existing.isEmpty()) {
            throw new RuntimeException("Вы уже зарегистрированы");
        }

        Registration reg = new Registration();
        reg.setIdUser(user);
        reg.setIdEvent(event);
        registrationRepository.save(reg);

        event.setCurrentParticipants(event.getCurrentParticipants() + 1);
        eventRepository.save(event);
    }

    private EventDTO toDTO(Event event) {
        EventDTO dto = new EventDTO();
        dto.setId(event.getIdEvent());
        dto.setTitle(event.getTitle());
        dto.setDescription(event.getDescription());
        dto.setEventDate(event.getEventDate());
        dto.setMaxParticipants(event.getMaxParticipants());
        dto.setCurrentParticipants(event.getCurrentParticipants());
        dto.setStatus(event.getStatus());

        if (event.getIdVenue() != null) {
            dto.setVenueId(event.getIdVenue().getIdVenue());
            dto.setVenueName(event.getIdVenue().getName());
        }

        if (event.getIdOrganizer() != null) {
            dto.setOrganizerId(event.getIdOrganizer().getIdUser());
            dto.setOrganizerName(event.getIdOrganizer().getName());
        }

        return dto;
    }
}