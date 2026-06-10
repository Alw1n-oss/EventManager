package ru.hse.eventmanager.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hse.eventmanager.model.Event;
import ru.hse.eventmanager.model.User;
import ru.hse.eventmanager.repository.RegistrationRepository;
import ru.hse.eventmanager.repository.EventRepository;
import ru.hse.eventmanager.repository.UserRepository;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegistrationController.class)
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistrationRepository registrationRepository;

    @MockBean
    private EventRepository eventRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testRegisterSuccess() throws Exception {
        Event event = new Event();
        event.setIdEvent(1);
        event.setMaxParticipants(10);
        event.setCurrentParticipants(5);

        User user = new User();
        user.setIdUser(1);

        when(eventRepository.findById(1)).thenReturn(Optional.of(event));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(registrationRepository.findByIdUserAndIdEvent(user, event))
            .thenReturn(java.util.Collections.emptyList());

        mockMvc.perform(post("/api/registrations")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"idUser\":1,\"idEvent\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Успешно зарегистрированы"));
    }
    
    @Test
    public void testRegisterLimitExceeded() throws Exception {
        Event event = new Event();
        event.setIdEvent(1);
        event.setMaxParticipants(5);
        event.setCurrentParticipants(5);

        User user = new User();
        user.setIdUser(1);

        when(eventRepository.findById(1)).thenReturn(Optional.of(event));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/registrations")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"idUser\":1,\"idEvent\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Ошибка: мест нет"));
    }
}