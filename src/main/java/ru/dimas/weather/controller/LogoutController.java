package ru.dimas.weather.controller;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.dimas.weather.service.withdb.SessionService;
import ru.dimas.weather.service.withdb.UserService;

import java.util.UUID;

@Controller
@RequestMapping("/logout")
public class LogoutController {

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);
    private final SessionService sessionService;
//    private final UserService userService;

    public LogoutController(SessionService sessionService, UserService userService) {
        this.sessionService = sessionService;
//        this.userService = userService;
    }

    @PostMapping
    public String logoutUser(HttpSession httpSession, Model model) {
        logger.info("logoutUser with id: {}", httpSession.getAttribute("userId"));
        sessionService.deleteSession((UUID) httpSession.getAttribute("sessionId"));
        httpSession.removeAttribute("userId");
        httpSession.removeAttribute("sessionId");
        logger.info("user has logged out");
        return "index"; // Имя шаблона
    }
}
