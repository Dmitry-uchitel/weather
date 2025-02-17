package ru.dimas.weather.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.dimas.weather.model.Session;
import ru.dimas.weather.repository.SessionRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class SessionService {
    @Value("${weather.session.time}")
    private Long sessionTime; // Загружается из application.properties

    private final SessionRepository sessionRepository;
    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public Optional<Session> getSessionById(UUID id) {
        return sessionRepository.findById(id);
    }
    public Session createSession(Session session) {return sessionRepository.save(session);}
    public void deleteSession(UUID id) {
        sessionRepository.deleteById(id);
    }
    public void deleteEndedSessions() {sessionRepository.deleteAllByExpiresAtBefore(LocalDateTime.now().minusMinutes(sessionTime));}
}
