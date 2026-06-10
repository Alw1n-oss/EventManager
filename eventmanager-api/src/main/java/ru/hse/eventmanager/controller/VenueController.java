package ru.hse.eventmanager.controller;

import org.springframework.web.bind.annotation.*;
import ru.hse.eventmanager.dto.VenueDTO;
import ru.hse.eventmanager.model.Venue;
import ru.hse.eventmanager.repository.VenueRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/venues")
@CrossOrigin(origins = "*")
public class VenueController {

    private final VenueRepository venueRepository;

    public VenueController(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    @GetMapping
    public List<VenueDTO> getAllVenues() {
        return venueRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    private VenueDTO toDTO(Venue venue) {
        VenueDTO dto = new VenueDTO();
        dto.setIdVenue(venue.getIdVenue());
        dto.setName(venue.getName());
        dto.setAddress(venue.getAddress());
        dto.setCapacity(venue.getCapacity());
        return dto;
    }
}