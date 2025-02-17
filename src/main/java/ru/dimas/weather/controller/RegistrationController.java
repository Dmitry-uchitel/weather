package ru.dimas.weather.controller;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.dimas.weather.exception.UserAlreadyExistsException;
import ru.dimas.weather.model.User;
import ru.dimas.weather.service.SessionService;
import ru.dimas.weather.service.UserService;
import ru.dimas.weather.util.CreateSessionForUser;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
    private final SessionService sessionService;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    public RegistrationController(SessionService sessionService, UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
    }

    @GetMapping
    public String showRegistrationPage(Model model) {
        logger.info("show Registration page");
        return "registration"; // Отображение страницы регистрации
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String registrationUser(@RequestBody @Validated User user, HttpSession httpSession) {
        logger.info("registrationUser method called with userName: {}", user.getLogin());
        // Проверяем, существует ли пользователь с таким логином
        if (userService.userExists(user.getLogin())) {
            logger.error("User with login {} already exist", user.getLogin());
            throw new UserAlreadyExistsException("User with this login already exists");
        }
        userService.createUser(user);
        CreateSessionForUser.createSesion(user, httpSession, sessionService);
        logger.info("User registration is end");
        // Перенаправление на домашнюю страницу
        return "redirect:/weather?id=" + user.getId(); // Передача ID пользователя
    }
}
