package ru.dimas.weather.controller;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.dimas.weather.service.withdb.SessionService;

import java.util.UUID;

@Controller
@RequestMapping("/logout")
public class LogoutController {

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);
    private final SessionService sessionService;

    public LogoutController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping
    public String showIndexPage() {
        logger.info("show Index page");
        return "index"; // Имя шаблона
    }

    @PostMapping
    public String logoutUser(HttpSession httpSession) {
        logger.info("logoutUser with id: {}", httpSession.getAttribute("userId"));
        UUID sessionId = (UUID) httpSession.getAttribute("sessionId");
        if (sessionId != null) {
            sessionService.deleteSession((UUID) httpSession.getAttribute("sessionId"));
            httpSession.removeAttribute("userId");
            httpSession.removeAttribute("sessionId");
        }
        logger.info("user has logged out");
        return "index"; // Имя шаблона
    }
}
