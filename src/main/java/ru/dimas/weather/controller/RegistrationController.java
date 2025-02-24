package ru.dimas.weather.controller;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.dimas.weather.DTO.UserDto;
import ru.dimas.weather.exception.UserAlreadyExistsException;
import ru.dimas.weather.model.User;
import ru.dimas.weather.service.withoutdb.LoginService;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private final LoginService loginService;
    private final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    public RegistrationController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping
    public String showRegistrationPage(Model model) {
        logger.info("show Registration page");
        model.addAttribute("userDto", new UserDto());
        return "registration"; // Отображение страницы регистрации
    }

    @PostMapping
    public String registrationUser(@ModelAttribute UserDto userDto, HttpSession httpSession, Model model) {
        logger.info("Attempting to registration with username {} and password {}", userDto.getLogin(), userDto.getPassword());
        User user = new User(userDto);
        try {
            loginService.registration(user, httpSession);
        } catch (UserAlreadyExistsException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "/registration";
        }
        return "redirect:/weather/" + httpSession.getAttribute("userId");
    }
}
