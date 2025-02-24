package ru.dimas.weather.controller;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.dimas.weather.exception.UserWithLoginIsNotExist;
import ru.dimas.weather.exception.WrongPasswordException;
import ru.dimas.weather.DTO.UserDto;
import ru.dimas.weather.model.User;
import ru.dimas.weather.service.withoutdb.LoginService;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;
    private final Logger logger = LoggerFactory.getLogger(WeatherController.class);
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping
    public String showLoginPage(Model model) {
        logger.info("show Login page");
        model.addAttribute("userDto", new UserDto());
        return "login"; // Отображение страницы регистрации
    }

    @PostMapping
    public String loginUser(@ModelAttribute UserDto userDto, HttpSession httpSession, Model model) {
        logger.info("Attempting to login with username {} and password {}", userDto.getLogin(), userDto.getPassword());
        User user = new User(userDto);
        try {
            loginService.loginUser(user, httpSession);
        } catch (UserWithLoginIsNotExist | WrongPasswordException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "/login";
        }
        return "redirect:/weather/" + httpSession.getAttribute("userId");
    }
}
