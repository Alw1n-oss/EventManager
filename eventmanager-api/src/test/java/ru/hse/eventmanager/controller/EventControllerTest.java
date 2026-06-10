package ru.hse.eventmanager.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hse.eventmanager.dto.EventDTO;
import ru.hse.eventmanager.service.EventService;

import java.util.Arrays;
import java.util.Date;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventController.class)
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Test
    public void testGetAllEvents() throws Exception {
        EventDTO dto = new EventDTO();
        dto.setId(1);
        dto.setTitle("Встреча клуба Java");
        dto.setEventDate(new Date());
        dto.setMaxParticipants(30);
        dto.setCurrentParticipants(5);
        dto.setVenueName("Аудитория 101");

        when(eventService.findAll()).thenReturn(Arrays.asList(dto));

        mockMvc.perform(get("/api/events")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Встреча клуба Java"));
    }

    @Test
    public void testGetEventById() throws Exception {
        EventDTO dto = new EventDTO();
        dto.setId(1);
        dto.setTitle("Мастер-класс");

        when(eventService.findById(1)).thenReturn(dto);

        mockMvc.perform(get("/api/events/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Мастер-класс"));
    }
}