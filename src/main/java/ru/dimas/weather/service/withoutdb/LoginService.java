package ru.dimas.weather.service.withoutdb;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.dimas.weather.controller.WeatherController;
import ru.dimas.weather.exception.UserWithLoginIsNotExist;
import ru.dimas.weather.exception.WrongPasswordException;
import ru.dimas.weather.model.Session;
import ru.dimas.weather.model.User;
import ru.dimas.weather.service.withdb.SessionService;
import ru.dimas.weather.service.withdb.UserService;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

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
        String password = user.getPassword();
        try {
            user = userService.getUserByLogin(user.getLogin()).get();
        } catch (NoSuchElementException e) {
            logger.error("A user with login: {} does not exist", user.getLogin());
            throw new UserWithLoginIsNotExist("A user with this login does not exist");
        }
        if (!password.equals(user.getPassword())) {
            logger.error("A user with login: {} Wrong password", user.getLogin());
            throw new WrongPasswordException("Wrong password");
        }
        createSesion(user, httpSession, sessionService);
        logger.info("User login with id: {} completed", httpSession.getAttribute("userId"));
    }

    public void createSesion(User user, HttpSession httpSession, SessionService sessionService) {
        Session session = new Session(user, LocalDateTime.now().plusMinutes(sessionTime));
        sessionService.createSession(session);
        httpSession.setAttribute("userId", user.getId());
        httpSession.setAttribute("userLogin", user.getLogin());
        httpSession.setAttribute("sessionId", session.getId());
        logger.info("A session: {} has been created for the user with login: {} and id: {}", session.getId(), user.getLogin(), user.getId());
    }

    public void registration(User user, HttpSession httpSession) {
        logger.info("registrationUser method called with userName: {}", user.getLogin());
        userService.createUser(user);
        createSesion(user, httpSession, sessionService);
        logger.info("registrationUser with id:{} is completed", user.getId());
    }
}
