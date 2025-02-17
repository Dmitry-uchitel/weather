package ru.dimas.weather.service;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import ru.dimas.weather.controller.WeatherController;
import ru.dimas.weather.exception.SessionIsAlreadyOverException;
import ru.dimas.weather.model.Session;
import ru.dimas.weather.service.SessionService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SessionСheckService {


    private final Logger logger = LoggerFactory.getLogger(WeatherController.class);
    @Value("${weather.session.time}")
    private Long sessionTime; // Загружается из application.properties

    public boolean sessionIsExistAndActive(UUID sessionId, SessionService sessionService) {
        if (sessionId==null){
            logger.error("Session is not exist in httpSession");
            return false;
        }
        Session session = null;
        try {
            session = sessionService.getSessionById(sessionId).get();
        }
        catch (InvalidDataAccessApiUsageException e){
            logger.error("Session is not exist in database");
            return false;
        }
        logger.info("time, when session end: {}\t now: {}", session.getExpiresAt().plusMinutes(sessionTime),LocalDateTime.now());
        return session.getExpiresAt().plusMinutes(sessionTime).isAfter(LocalDateTime.now());
    }

    public void checkSession(SessionService sessionService, HttpSession httpSession) {
        UUID sessionId = (UUID) httpSession.getAttribute("sessionId");
        if (!sessionIsExistAndActive(sessionId, sessionService)) {
            logger.error("Session already is over");
            sessionService.deleteEndedSessions();
            throw new SessionIsAlreadyOverException("Session already is over");
        }
    }
}
