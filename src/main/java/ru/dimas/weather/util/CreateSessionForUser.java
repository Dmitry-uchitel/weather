package ru.dimas.weather.util;

import jakarta.servlet.http.HttpSession;
import ru.dimas.weather.model.Session;
import ru.dimas.weather.model.User;
import ru.dimas.weather.service.SessionService;

import java.time.LocalDateTime;

public class CreateSessionForUser {
    public static String createSesion(User user, HttpSession httpSession, SessionService sessionService) {
        Session session = new Session();
        session.setUser(user);
        session.setExpiresAt(LocalDateTime.now());
        sessionService.createSession(session);
        httpSession.setAttribute("userID", user.getId());
        httpSession.setAttribute("sessionID", session.getId());
        return "redirect:/weather-page/" + user.getId();
    }
}
