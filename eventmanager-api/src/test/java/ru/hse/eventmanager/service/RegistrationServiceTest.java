package ru.hse.eventmanager.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hse.eventmanager.model.Event;
import ru.hse.eventmanager.model.Registration;
import ru.hse.eventmanager.model.User;
import ru.hse.eventmanager.repository.EventRepository;
import ru.hse.eventmanager.repository.RegistrationRepository;
import ru.hse.eventmanager.repository.UserRepository;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private RegistrationRepository registrationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RegistrationService registrationService;

    @Test
    public void testRegisterLimitExceeded() {
        Event event = new Event();
        event.setIdEvent(1);
        event.setMaxParticipants(5);
        event.setCurrentParticipants(5);

        User user = new User();
        user.setIdUser(1);

        when(eventRepository.findById(1)).thenReturn(Optional.of(event));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            registrationService.register(1, 1);
        });

        assertEquals("Мероприятие заполнено", exception.getMessage());
    }
    
    @Test
    public void testRegisterSuccess() {
        Event event = new Event();
        event.setIdEvent(1);
        event.setMaxParticipants(10);
        event.setCurrentParticipants(5);

        User user = new User();
        user.setIdUser(1);

        when(eventRepository.findById(1)).thenReturn(Optional.of(event));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> registrationService.register(1, 1));
        assertEquals(6, event.getCurrentParticipants());
    }
    
    @Test
    public void testRegisterAlreadyRegistered() {
        Event event = new Event();
        event.setIdEvent(1);
        event.setMaxParticipants(10);
        event.setCurrentParticipants(5);

        User user = new User();
        user.setIdUser(1);

        Registration existing = new Registration();
        existing.setIdUser(user);
        existing.setIdEvent(event);

        when(eventRepository.findById(1)).thenReturn(Optional.of(event));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(registrationRepository.findByIdUserAndIdEvent(user, event))
            .thenReturn(Arrays.asList(existing));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            registrationService.register(1, 1);
        });

        assertEquals("Вы уже зарегистрированы", exception.getMessage());
    }
}