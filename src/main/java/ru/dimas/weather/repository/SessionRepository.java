package ru.dimas.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.dimas.weather.model.Session;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {
    // Дополнительные методы могут быть добавлены здесь, если нужны кастомные запросы.
    // Например, поиск сессии по ID пользователя:
    Optional<Session> findByUserIdAndExpiresAtAfter(Long userId, LocalDateTime now);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM sessions WHERE expires_at < :localDateTime", nativeQuery = true)
    void deleteAllByExpiresAtBefore(@Param("localDateTime") LocalDateTime localDateTime);

}