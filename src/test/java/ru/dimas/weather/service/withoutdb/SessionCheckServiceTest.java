package ru.dimas.weather.service.withoutdb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.dimas.weather.exception.UserAlreadyExistsException;
import ru.dimas.weather.model.User;
import ru.dimas.weather.repository.UserRepository;
import ru.dimas.weather.service.withdb.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test") // Использование профиля "test"
@DataJpaTest // Автоматическая настройка Spring Data JPA с H2
class SesionCheckServiceTest {

    @Autowired
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository); // Инициализация сервиса
    }

    @Test
    void testCreateUser_Success() {
        // Arrange
        User user = new User();
        user.setLogin("testUser");
        user.setPassword("password123");

        // Act
        userService.createUser(user);

        // Assert
        Optional<User> savedUser = userRepository.findByLogin("testUser");
        assertTrue(savedUser.isPresent());
        assertEquals("testUser", savedUser.get().getLogin());
        assertEquals("password123", savedUser.get().getPassword());
    }

    @Test
    void testCreateUser_DuplicateLogin_Failure() {
        // Arrange
        User user1 = new User();
        user1.setLogin("duplicateUser");
        user1.setPassword("password123");

        User user2 = new User();
        user2.setLogin("duplicateUser");
        user2.setPassword("anotherPassword");

        // Act & Assert
        userService.createUser(user1); // Первый пользователь успешно создается
        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.createUser(user2); // Второй пользователь вызывает исключение
        });
    }
}