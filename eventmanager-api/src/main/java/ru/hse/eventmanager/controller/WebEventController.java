package ru.hse.eventmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.hse.eventmanager.dto.EventDTO;
import ru.hse.eventmanager.dto.UserDTO;
import ru.hse.eventmanager.service.AuthService;
import ru.hse.eventmanager.service.EventService;
import ru.hse.eventmanager.service.RegistrationService;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/web")
public class WebEventController {

    private final EventService eventService;
    private final RegistrationService registrationService;
    private final AuthService authService;

    public WebEventController(EventService eventService,
                              RegistrationService registrationService,
                              AuthService authService) {
        this.eventService = eventService;
        this.registrationService = registrationService;
        this.authService = authService;
    }

    // === АВТОРИЗАЦИЯ ===

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String login,
                          @RequestParam String password,
                          HttpSession session,
                          RedirectAttributes ra) {
        try {
            UserDTO user = authService.login(login, password);
            session.setAttribute("userId", user.getIdUser());
            session.setAttribute("userName", user.getName());
            session.setAttribute("userRole", user.getRoleName());
            return "redirect:/web/events";
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Неверный логин или пароль");
            return "redirect:/web/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/web/events";
    }

    // === КАТАЛОГ С ФИЛЬТРАМИ И СОРТИРОВКОЙ ===

    @GetMapping("/events")
    public String catalog(@RequestParam(required = false) String search,
                          @RequestParam(required = false) String dateFrom,
                          @RequestParam(required = false) String dateTo,
                          @RequestParam(required = false, defaultValue = "date") String sort,
                          Model model,
                          HttpSession session) {

        // Получаем все активные мероприятия (в учебной версии фильтрация в памяти)
        List<EventDTO> events = eventService.findAllActive();

        // Фильтр по названию
        if (search != null && !search.trim().isEmpty()) {
            String lower = search.toLowerCase();
            events = events.stream()
                    .filter(e -> e.getTitle() != null && e.getTitle().toLowerCase().contains(lower))
                    .collect(Collectors.toList());
        }

        // Фильтр по диапазону дат
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (dateFrom != null && !dateFrom.isEmpty()) {
            try {
                Date from = sdf.parse(dateFrom);
                events = events.stream()
                        .filter(e -> e.getEventDate() != null && !e.getEventDate().before(from))
                        .collect(Collectors.toList());
            } catch (ParseException ignored) {}
        }
        if (dateTo != null && !dateTo.isEmpty()) {
            try {
                Date to = sdf.parse(dateTo);
                events = events.stream()
                        .filter(e -> e.getEventDate() != null && !e.getEventDate().after(to))
                        .collect(Collectors.toList());
            } catch (ParseException ignored) {}
        }

        // Сортировка
        switch (sort) {
            case "name":
                events.sort(Comparator.comparing(e -> e.getTitle().toLowerCase()));
                break;
            case "places":
                events.sort(Comparator.comparingInt(e -> e.getMaxParticipants() - e.getCurrentParticipants()));
                break;
            default: // date
                events.sort(Comparator.comparing(EventDTO::getEventDate));
        }

        model.addAttribute("events", events);
        model.addAttribute("search", search);
        model.addAttribute("dateFrom", dateFrom);
        model.addAttribute("dateTo", dateTo);
        model.addAttribute("sort", sort);
        model.addAttribute("userName", session.getAttribute("userName"));

        return "events/catalog";
    }

    // === ДЕТАЛИ МЕРОПРИЯТИЯ ===

    @GetMapping("/events/{id}")
    public String details(@PathVariable Integer id,
                          Model model,
                          HttpSession session) {
        EventDTO event = eventService.findById(id);
        model.addAttribute("event", event);
        model.addAttribute("remaining", event.getMaxParticipants() - event.getCurrentParticipants());
        model.addAttribute("canRegister", event.getCurrentParticipants() < event.getMaxParticipants());
        model.addAttribute("isLoggedIn", session.getAttribute("userId") != null);
        return "events/details";
    }

    // === РЕГИСТРАЦИЯ ===

    @PostMapping("/events/{id}/register")
    public String register(@PathVariable Integer id,
                           HttpSession session,
                           RedirectAttributes ra) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            ra.addFlashAttribute("error", "Для регистрации необходимо войти в систему");
            return "redirect:/web/login";
        }

        try {
            registrationService.register(userId, id);
            ra.addFlashAttribute("message", "Вы успешно зарегистрированы на мероприятие!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/web/events/" + id;
    }
}