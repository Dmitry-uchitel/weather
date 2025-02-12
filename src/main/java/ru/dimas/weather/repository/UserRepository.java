package ru.dimas.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dimas.weather.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Дополнительные методы могут быть добавлены здесь, если нужны кастомные запросы.
    // Например, поиск пользователя по логину:
    Optional<User> findByLogin(String login);

    // Проверка существования пользователя по логину:
    boolean existsByLogin(String login);

}