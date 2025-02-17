package ru.dimas.weather.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.Map;

@Controller
@RequestMapping("/session")
public class SessionController {

    @GetMapping("/userId")
    public ResponseEntity<Map<String, Object>> getUserId(HttpSession httpSession) {
        Long userId = (Long) httpSession.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", "User ID not found"));
        }

        return ResponseEntity.ok(Collections.singletonMap("userId", userId));
    }
}