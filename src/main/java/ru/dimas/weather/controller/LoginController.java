package ru.dimas.weather.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.dimas.weather.exeption.WrongPasswordException;
import ru.dimas.weather.exeption.UserWithLoginIsNotExist;
import ru.dimas.weather.model.Session;
import ru.dimas.weather.model.User;
import ru.dimas.weather.service.SessionService;
import ru.dimas.weather.service.UserService;
import ru.dimas.weather.util.CreateSessionForUser;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/login")
public class LoginController {
    private final SessionService sessionService;
    private final UserService userService;

    public LoginController(SessionService sessionService, UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
    }

    @PostMapping
    public String loginUser(@RequestBody @Validated User user, HttpSession httpSession) {
        // Проверяем, существует ли пользователь с таким логином
        if (!userService.userExists(user.getLogin())) {
            throw new UserWithLoginIsNotExist("A user with this login does not exist");
        }
        if (!userService.userExists(user.getLogin())) {
            throw new WrongPasswordException("Wrong password");
        }
        user = userService.getUserByLogin(user.getLogin()).get();

        return CreateSessionForUser.createSesion(user, httpSession, sessionService);
    }
}
