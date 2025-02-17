package ru.dimas.weather.controller;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.dimas.weather.deserialization.City;
import ru.dimas.weather.deserialization.WeatherMain;
import ru.dimas.weather.model.Location;
import ru.dimas.weather.model.User;
import ru.dimas.weather.service.LocationService;
import ru.dimas.weather.service.SessionService;
import ru.dimas.weather.service.UserService;
import ru.dimas.weather.util.GetWeather;
import ru.dimas.weather.util.GetWeatherForUser;
import ru.dimas.weather.service.SessionСheckService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/weather")
public class WeatherController {

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);
    private final SessionService sessionService;
    private final UserService userService;
    private final LocationService locationService;
    private final SessionСheckService sessionСheckService;

    @Value("${openweathermap.api.key}")
    private String apiKey; // Загружается из application.properties

    public WeatherController(SessionService sessionService, UserService userService, LocationService locationService, SessionСheckService sessionСheckService) {
        this.sessionService = sessionService;
        this.userService = userService;
        this.locationService = locationService;
        this.sessionСheckService = sessionСheckService;
    }

    @GetMapping("/{id}")
    public String showUserWeatherPage(@PathVariable long id, Model model, HttpSession httpSession) {
        logger.info("showUserWeatherPage method called for User with ID {}", id);
        sessionСheckService.checkSession(sessionService, httpSession);
        GetWeatherForUser.getAllWeatherForUser(model, id, userService, locationService, apiKey);
        logger.info("showUserWeatherPage method is finished for User with ID {}", id);
        return "weather"; // Имя шаблона
    }

    @GetMapping("/{id}/add")
    @ResponseBody
    public ResponseEntity<List<City>> searchLocations(@PathVariable long id, @RequestParam String locationName, HttpSession httpSession) {
        logger.info("searchLocations method called with userId: {}, locationName: {}", id, locationName);
        sessionСheckService.checkSession(sessionService, httpSession);

        ResponseEntity<List<City>> response = GetWeather.checkLocation(locationName, apiKey);
        if (response.getStatusCode().is2xxSuccessful()) {
            logger.info("Successfully checked location: {}", locationName);
        } else {
            logger.warn("Failed to check location: {}", locationName);
        }
        return response;
    }

    @PostMapping("/{id}/add")
    @ResponseBody
    public ResponseEntity<String> addLocation(@PathVariable long id, @RequestBody City city, HttpSession httpSession) {
        logger.info("addLocation method called with userId: {}, location: {}", id, city);
        sessionСheckService.checkSession(sessionService, httpSession);


        Optional<User> userOptional = userService.getUserById(id);
        if (userOptional.isEmpty()) {
            logger.error("User with ID {} not found", id);
            return ResponseEntity.status(404).body("User not found");
        }

        User user = userOptional.get();
        Location location = new Location();
        location.setLongitude((BigDecimal) city.getLon());
        location.setLatitude((BigDecimal) city.getLat());
        location.setName(city.getName());
        location.setUser(user);


        if (locationService.isExistForUser(id, location)) {
            logger.error("User with ID {} already has this location {}", id, city.getName());
            return ResponseEntity.status(409).body("Location already added");
        }

        locationService.createLocation(location);
        logger.info("Location added successfully: {}", location);

        return ResponseEntity.ok("Location added successfully");
    }

    @PostMapping("/{id}/delete")
    @ResponseBody
    public String deleteLocation(@PathVariable long id, @RequestBody WeatherMain weatherMain, HttpSession httpSession, Model model) {
        logger.info("deleteLocation method called with userId: {}, weather with Id: {}", id, weatherMain.getName());
        sessionСheckService.checkSession(sessionService, httpSession);

        locationService.deleteLocationById((Long) weatherMain.getIdFromDatabase());
        return "weather";
    }
}
