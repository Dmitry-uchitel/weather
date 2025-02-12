package ru.dimas.weather.service;

import org.springframework.stereotype.Service;
import ru.dimas.weather.exeption.SessionIsAlreadyOverException;
import ru.dimas.weather.model.Session;
import ru.dimas.weather.repository.SessionRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class SessionService {

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


//    public boolean isExist(UUID id){
//
//        if (sessionRepository.findById(id).get().getExpiresAt().plusMinutes(30)
//                .isAfter(LocalDateTime.now())){
//        }
//        else {
//            throw new SessionIsAlreadyOverException("Session is already over!!!");
//        }
//    }

}
