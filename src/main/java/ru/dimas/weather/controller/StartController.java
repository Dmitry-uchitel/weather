package ru.dimas.weather.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/dimas")
public class StartController {
    @GetMapping()
    public String home(Model model) {
//        model.addAttribute("message", "Welcome to the Home Page!");
        return "error"; // Возвращает шаблон `templates/home.html`
    }
}
