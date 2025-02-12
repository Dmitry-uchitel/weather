package ru.dimas.weather.util;

import ru.dimas.weather.model.Session;
import ru.dimas.weather.service.SessionService;
import ru.dimas.weather.service.UserService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class SessionСheck {
    public static boolean sessionIsExistAndActive(UUID sessionId, SessionService sessionService) {
        if (sessionId==null){
            return false;
        }
        Optional<Session> session = sessionService.getSessionById(sessionId);
        if (session.isEmpty()){
            return false;
        }
        if (session.get().getExpiresAt().plusMinutes(30).isBefore(LocalDateTime.now())){
            return false;
        }
        return true;
    }
}
