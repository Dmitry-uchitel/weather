package ru.dimas.weather.service.withoutdb;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.dimas.weather.controller.WeatherController;
import ru.dimas.weather.exception.UserAlreadyExistsException;
import ru.dimas.weather.exception.UserWithLoginIsNotExist;
import ru.dimas.weather.exception.WrongPasswordException;
import ru.dimas.weather.model.Session;
import ru.dimas.weather.model.User;
import ru.dimas.weather.service.withdb.SessionService;
import ru.dimas.weather.service.withdb.UserService;

import java.time.LocalDateTime;

@Service
public class LoginService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);
    private final UserService userService;
    private final SessionService sessionService;

    @Value("${weather.session.time}")
    private Long sessionTime; // Загружается из application.properties

    public LoginService(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    public void loginUser(User user, HttpSession httpSession) {
        logger.info("loginUser method called with userName: {}", user.getLogin());
        // Проверяем, существует ли пользователь с таким логином


        if (!userService.userExists(user.getLogin())) {
            logger.error("A user with login: {} does not exist", user.getLogin());
            throw new UserWithLoginIsNotExist("A user with this login does not exist");
        }
        // Проверяем, существует ли пользователь с таким паролем
        String password = user.getPassword();
        user = userService.getUserByLogin(user.getLogin()).get();
        if (!password.equals(user.getPassword())) {
            logger.error("A user with login: {} Wrong password", user.getLogin());
            throw new WrongPasswordException("Wrong password");
        }
        createSesion(user, httpSession, sessionService);
        logger.info("User login with id end", httpSession.getAttribute("userId"));
    }

    public void createSesion(User user, HttpSession httpSession, SessionService sessionService) {
        Session session = new Session();
        session.setUser(user);
        session.setExpiresAt(LocalDateTime.now().plusMinutes(sessionTime));
        sessionService.createSession(session);
        httpSession.setAttribute("userId", user.getId());
        httpSession.setAttribute("userLogin", user.getLogin());
        httpSession.setAttribute("sessionId", session.getId());
        logger.info("A session: {} has been created for the user with login: {} and id: {}", session.getId(), user.getLogin(), user.getId());
    }

    public void registration(User user, HttpSession httpSession) {
        logger.info("registrationUser method called with userName: {}", user.getLogin());
        // Проверяем, существует ли пользователь с таким логином
        try {
            userService.createUser(user);
        } catch (DataIntegrityViolationException e) {
            logger.error("User with login {} already exist", user.getLogin());
            throw new UserAlreadyExistsException("User with this login already exists");
        }
        createSesion(user, httpSession, sessionService);
        logger.info("User registration is end");
    }
}
