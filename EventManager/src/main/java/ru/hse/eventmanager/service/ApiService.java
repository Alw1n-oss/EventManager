package ru.hse.eventmanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ru.hse.eventmanager.dto.UserDTO;
import ru.hse.eventmanager.dto.EventDTO;
import ru.hse.eventmanager.dto.AuthRequest;
import ru.hse.eventmanager.dto.VenueDTO;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class ApiService {
    private static final String BASE_URL = "http://localhost:8080/api";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private static UserDTO currentUser;

    public UserDTO login(String login, String password) throws Exception {
        AuthRequest req = new AuthRequest();
        req.setLogin(login);
        req.setPassword(password);
        String json = mapper.writeValueAsString(req);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/auth/login"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            currentUser = mapper.readValue(response.body(), UserDTO.class);
            return currentUser;
        }
        throw new RuntimeException("Неверный логин или пароль");
    }

    public List<EventDTO> getAllEvents() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/events"))
            .header("Accept", "application/json")
            .GET()
            .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), 
            mapper.getTypeFactory().constructCollectionType(List.class, EventDTO.class));
    }

    public List<UserDTO> getAllUsers() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/users"))
            .header("Accept", "application/json")
            .GET()
            .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), 
            mapper.getTypeFactory().constructCollectionType(List.class, UserDTO.class));
    }

    public UserDTO updateUser(UserDTO user) throws Exception {
        String json = mapper.writeValueAsString(user);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/users/" + user.getIdUser()))
            .header("Content-Type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(json))
            .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), UserDTO.class);
    }

    public List<VenueDTO> getAllVenues() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/venues"))
            .header("Accept", "application/json")
            .GET()
            .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), 
            mapper.getTypeFactory().constructCollectionType(List.class, VenueDTO.class));
    }

    public EventDTO createEvent(EventDTO dto) throws Exception {
        String json = mapper.writeValueAsString(dto);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/events"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), EventDTO.class);
    }
    public UserDTO createUser(UserDTO user) throws Exception {
    String json = mapper.writeValueAsString(user);
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(BASE_URL + "/users"))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(json))
        .build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    return mapper.readValue(response.body(), UserDTO.class);
}

    public void deleteUser(Integer id) throws Exception {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(BASE_URL + "/users/" + id))
        .DELETE()
        .build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    if (response.statusCode() != 200 && response.statusCode() != 204) {
        throw new RuntimeException("Delete failed: " + response.body());
    }
}
    
    public EventDTO updateEvent(EventDTO dto) throws Exception {
        String json = mapper.writeValueAsString(dto);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/events/" + dto.getId()))  // было getIdEvent()
            .header("Content-Type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(json))
            .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), EventDTO.class);
    }

    public String registerForEvent(Integer userId, Integer eventId) throws Exception {
        // ИСПРАВЛЕНО: шлём JSON вместо query params
        Map<String, Integer> body = Map.of("idUser", userId, "idEvent", eventId);
        String json = mapper.writeValueAsString(body);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/registrations"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
    
    public void deleteEvent(Integer id) throws Exception {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(BASE_URL + "/events/" + id))
        .DELETE()
        .build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    if (response.statusCode() != 200 && response.statusCode() != 204) {
           throw new RuntimeException("Delete failed: " + response.body());
        }
    }

    public static UserDTO getCurrentUser() { return currentUser; }
    public static void setCurrentUser(UserDTO user) { currentUser = user; }
}