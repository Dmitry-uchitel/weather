package ru.dimas.weather.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.dimas.weather.exeption.UserAlreadyExistsException;
import ru.dimas.weather.model.Session;
import ru.dimas.weather.model.User;
import ru.dimas.weather.service.SessionService;
import ru.dimas.weather.service.UserService;
import ru.dimas.weather.util.CreateSessionForUser;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
    private final SessionService sessionService;
    private final UserService userService;

    public RegistrationController(SessionService sessionService, UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
    }


    @PostMapping
    public String createUser(@RequestBody @Validated User user, HttpSession httpSession) {
        // Проверяем, существует ли пользователь с таким логином
        if (userService.userExists(user.getLogin())) {
            throw new UserAlreadyExistsException("User with this login already exists");
        }
        userService.createUser(user);
        return CreateSessionForUser.createSesion(user, httpSession, sessionService);
    }
}
