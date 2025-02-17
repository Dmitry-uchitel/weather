package ru.dimas.weather.util;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dimas.weather.controller.WeatherController;
import ru.dimas.weather.model.Session;
import ru.dimas.weather.model.User;
import ru.dimas.weather.service.SessionService;

import java.time.LocalDateTime;

public class CreateSessionForUser {

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    public static void createSesion(User user, HttpSession httpSession, SessionService sessionService) {
        Session session = new Session();
        session.setUser(user);
        session.setExpiresAt(LocalDateTime.now());
        sessionService.createSession(session);
        httpSession.setAttribute("userId", user.getId());
        httpSession.setAttribute("sessionId", session.getId());
        logger.info("A session: {} has been created for the user with login: {} and id: {}", session.getId(), user.getLogin(), user.getId());

    }
}
