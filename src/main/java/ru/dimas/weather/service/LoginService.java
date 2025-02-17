package ru.dimas.weather.service;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.dimas.weather.controller.WeatherController;
import ru.dimas.weather.exception.UserWithLoginIsNotExist;
import ru.dimas.weather.exception.WrongPasswordException;
import ru.dimas.weather.model.User;
import ru.dimas.weather.util.CreateSessionForUser;

@Service
public class LoginService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);
    private final UserService userService;
    private final SessionService sessionService;

    public LoginService(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    public HttpEntity<Object> loginUser(User user, HttpSession httpSession) {

        logger.info("loginUser method called with userName: {}", user.getLogin());
        // Проверяем, существует ли пользователь с таким логином
        if (!userService.userExists(user.getLogin())) {
            logger.error("A user with login: {} does not exist", user.getLogin());
            throw new UserWithLoginIsNotExist("A user with this login does not exist");
        }
        String password = user.getPassword();
        user = userService.getUserByLogin(user.getLogin()).get();
        if (!password.equals(user.getPassword())) {
            logger.error("A user with login: {} Wrong password", user.getLogin());
            throw new WrongPasswordException("Wrong password");
        }

        CreateSessionForUser.createSesion(user, httpSession, sessionService);
        logger.info("User login is end");

        return ResponseEntity.ok().build();
    }
}
