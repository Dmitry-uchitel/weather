package ru.dimas.weather.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);
    @GetMapping("/")
    public String showIndexPage() {
        logger.info("show Index page");
        return "index"; // Имя шаблона
    }
}