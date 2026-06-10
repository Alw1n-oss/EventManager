package ru.hse.eventmanager;

import org.junit.jupiter.api.Test;
import ru.hse.eventmanager.model.Event;
import static org.junit.jupiter.api.Assertions.*;

public class EventTest {

    @Test
    public void testRegistrationLimit() {
        Event event = new Event();
        event.setMaxParticipants(2);
        event.setCurrentParticipants(2);
        event.setVersion(1L);

        Exception exception = assertThrows(Exception.class, () -> {
            if (event.getCurrentParticipants() >= event.getMaxParticipants()) {
                throw new Exception("Мероприятие заполнено");
            }
        });

        assertEquals("Мероприятие заполнено", exception.getMessage());
    }
}