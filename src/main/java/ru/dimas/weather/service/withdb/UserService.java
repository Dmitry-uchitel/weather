package ru.dimas.weather.service.withdb;

import org.springframework.stereotype.Service;
import ru.dimas.weather.model.User;
import ru.dimas.weather.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    // Конструктор для внедрения зависимости
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public boolean userExists(String login) {
        return userRepository.existsByLogin(login);
    }


}