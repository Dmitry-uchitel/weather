package ru.dimas.weather.controller;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.dimas.weather.model.User;
import ru.dimas.weather.service.LoginService;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;
    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping
    public String showLoginPage(Model model) {
        logger.info("show Login page");
        return "login"; // Отображение страницы регистрации
    }

    @PostMapping
    public HttpEntity<Object> loginUser(@RequestBody @Validated User user, HttpSession httpSession) {
        logger.info("loginUser with login: {}", user.getLogin());

        return loginService.loginUser(user, httpSession);
    }
}
