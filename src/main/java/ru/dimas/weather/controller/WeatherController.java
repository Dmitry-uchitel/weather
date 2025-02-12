package ru.dimas.weather.controller;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.dimas.weather.deserialization.City;
import ru.dimas.weather.deserialization.WeatherMain;
import ru.dimas.weather.exeption.LocationAlreadyAddedException;
import ru.dimas.weather.exeption.SessionIsAlreadyOverException;
import ru.dimas.weather.exeption.UserWithLoginIsNotExist;
import ru.dimas.weather.exeption.WrongPasswordException;
import ru.dimas.weather.model.Location;
import ru.dimas.weather.model.User;
import ru.dimas.weather.service.LocationService;
import ru.dimas.weather.service.SessionService;
import ru.dimas.weather.service.UserService;
import ru.dimas.weather.util.CreateSessionForUser;
import ru.dimas.weather.util.GetWeather;
import ru.dimas.weather.util.SessionСheck;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/weather")
public class WeatherController {
    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    private final SessionService sessionService;
    private final UserService userService;
    private final LocationService locationService;

    @Value("${openweathermap.api.key}")
    private String apiKey; // Загружается из application.properties

    public WeatherController(SessionService sessionService, UserService userService, LocationService locationService) {
        this.sessionService = sessionService;
        this.userService = userService;
        this.locationService = locationService;
    }

    @GetMapping("/{id}")
    public List<ResponseEntity<WeatherMain>> getAllWeatherForUser(HttpSession httpSession, @PathVariable long id) {
        UUID sessionId = (UUID) httpSession.getAttribute("sessionID");

        // Логирование начала метода
        logger.info("getAllWeatherForUser method called with userId: {}", id);

        if (!SessionСheck.sessionIsExistAndActive(sessionId, sessionService)) {
            logger.error("Session is not active or does not exist for sessionID: {}", sessionId);
            throw new SessionIsAlreadyOverException("Session already is over");
        }

        Optional<User> userOptional = userService.getUserById(id);
        if (userOptional.isEmpty()) {
            logger.error("User with ID {} not found", id);
            throw new IllegalArgumentException("User not found");
        }

        Set<Location> locationSet = userOptional.get().getLocationSet();

        List<ResponseEntity<WeatherMain>> weatherResponses = locationSet.stream()
                .map(location -> GetWeather.getWeather(location, apiKey))
                .collect(Collectors.toList());

        logger.info("Successfully fetched weather data for {} locations", locationSet.size());
        return weatherResponses;
    }

    @GetMapping("/{id}/add")
    public ResponseEntity<List<City>> showLocations(HttpSession httpSession, @PathVariable long id, @RequestParam String locationName) {
        UUID sessionId = (UUID) httpSession.getAttribute("sessionID");

        // Логирование начала метода
        logger.info("showLocations method called with userId: {}, locationName: {}", id, locationName);

        if (!SessionСheck.sessionIsExistAndActive(sessionId, sessionService)) {
            logger.error("Session is not active or does not exist for sessionID: {}", sessionId);
            throw new SessionIsAlreadyOverException("Session already is over");
        }

        ResponseEntity<List<City>> response = GetWeather.checkLocation(locationName, apiKey);

        if (response.getStatusCode().is2xxSuccessful()) {
            logger.info("Successfully checked location: {}", locationName);
        } else {
            logger.warn("Failed to check location: {}", locationName);
        }

        return response;
    }

    @PostMapping("/{id}/add")
    public void addLocation(HttpSession httpSession, @PathVariable long id, @RequestBody @Validated City city) {
        UUID sessionId = (UUID) httpSession.getAttribute("sessionID");

        // Логирование начала метода
        logger.info("addLocation method called with userId: {}, location: {}", id, city);

        if (!SessionСheck.sessionIsExistAndActive(sessionId, sessionService)) {
            logger.error("Session is not active or does not exist for sessionID: {}", sessionId);
            throw new SessionIsAlreadyOverException("Session already is over");
        }



        Optional<User> userOptional = userService.getUserById(id);
        if (userOptional.isEmpty()) {
            logger.error("User with ID {} not found", id);
            throw new IllegalArgumentException("User not found");
        }
        Location location = new Location();
        location.setLongitude(city.getLon());
        location.setLatitude(city.getLat());
        location.setName(city.getName());
        location.setUser(userOptional.get());
        if (locationService.isExistForUser(id, location)){
            logger.error("User with ID {} already have this location {}", id, city.getName());
            throw new LocationAlreadyAddedException("This location already added for user");
        }
        locationService.createLocation(location);
        logger.info("Location added successfully: {}", location);
    }
}
