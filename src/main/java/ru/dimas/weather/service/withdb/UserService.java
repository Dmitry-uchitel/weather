package ru.dimas.weather.service.withdb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.dimas.weather.controller.WeatherController;
import ru.dimas.weather.exception.UserAlreadyExistsException;
import ru.dimas.weather.model.User;
import ru.dimas.weather.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(WeatherController.class);
    // Конструктор для внедрения зависимости
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(User user) {
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            logger.error("User with login {} already exist", user.getLogin());
            throw new UserAlreadyExistsException("User with this login already exists");
        }
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public boolean userExists(String login) {
        return userRepository.existsByLogin(login);
    }


}