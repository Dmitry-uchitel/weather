package ru.dimas.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dimas.weather.model.Location;

import java.math.BigDecimal;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    // Дополнительные методы могут быть добавлены здесь, если нужны кастомные запросы.
    // Например, поиск локаций по имени пользователя:
    Iterable<Location> findAllByUserId(Long userId);
    Iterable<Location> findByUserIdAndLatitudeAndLongitude(Long userId, BigDecimal latitude, BigDecimal longitude);
}