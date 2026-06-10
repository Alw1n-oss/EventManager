package ru.hse.eventmanager.controller;

import org.springframework.web.bind.annotation.*;
import ru.hse.eventmanager.dto.EventDTO;
import ru.hse.eventmanager.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventDTO> getAllEvents() {
        return eventService.findAll();
    }

    @GetMapping("/{id}")
    public EventDTO getEvent(@PathVariable Integer id) {
        return eventService.findById(id);
    }

    @PostMapping
    public EventDTO createEvent(@RequestBody EventDTO dto) {
        return eventService.create(dto);
    }

    @PutMapping("/{id}")
    public EventDTO updateEvent(@PathVariable Integer id, @RequestBody EventDTO dto) {
        return eventService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Integer id) {
        eventService.delete(id);
    }
}