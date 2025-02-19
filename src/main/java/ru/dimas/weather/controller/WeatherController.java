package ru.dimas.weather.controller;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import ru.dimas.weather.DTO.CityDto;
import ru.dimas.weather.DTO.WeatherDto;
import ru.dimas.weather.exception.LocationAlreadyAddedException;
import ru.dimas.weather.exception.LocationsFromApiNotFoundException;
import ru.dimas.weather.exception.UserNotFoundException;
import ru.dimas.weather.model.Location;
import ru.dimas.weather.model.User;
import ru.dimas.weather.service.withdb.LocationService;
import ru.dimas.weather.service.withdb.SessionService;
import ru.dimas.weather.service.withdb.UserService;
import ru.dimas.weather.service.withoutdb.GetWeather;
import ru.dimas.weather.service.withoutdb.GetWeatherForUser;
import ru.dimas.weather.service.withoutdb.SessionCheckService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/weather")
public class WeatherController {

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);
    private final SessionService sessionService;
    private final UserService userService;
    private final LocationService locationService;
    private final SessionCheckService sessionCheckService;
    private final GetWeatherForUser getWeatherForUser;

    @Value("${openweathermap.api.key}")
    private String apiKey; // Загружается из application.properties

    public WeatherController(SessionService sessionService, UserService userService,
                             LocationService locationService, SessionCheckService sessionCheckService,
                             GetWeatherForUser getWeatherForUser) {
        this.sessionService = sessionService;
        this.userService = userService;
        this.locationService = locationService;
        this.sessionCheckService = sessionCheckService;
        this.getWeatherForUser = getWeatherForUser;
    }

    @GetMapping("/{id}")
    public String showUserWeatherPage(Model model, HttpSession httpSession) {
        logger.info("showUserWeatherPage method called for User with ID {}", httpSession.getAttribute("userId"));
        sessionCheckService.checkSession(sessionService, httpSession);
        try {
            getWeatherForUser.getAllWeatherForUser(model, (Long) httpSession.getAttribute("userId"));
        } catch (LocationsFromApiNotFoundException | ResourceAccessException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        logger.info("showUserWeatherPage method is finished for User with ID {}", httpSession.getAttribute("userId"));
        return "weather"; // Имя шаблона
    }

    @GetMapping("/{id}/add")
    public String searchLocations(@RequestParam String locationName, HttpSession httpSession, Model model) {
        logger.info("searchLocations method called with userId: {}, locationName: {}", httpSession.getAttribute("userId"), locationName);
        sessionCheckService.checkSession(sessionService, httpSession);
        List<CityDto> cityDtoList = GetWeather.checkLocation(locationName, apiKey);
        model.addAttribute("cityDtoList", cityDtoList);
        model.addAttribute("isAddPage", true); // Указываем, что это страница добавления
        return "weather";
    }

    @PostMapping("/{id}/add")
    public String addLocation(@ModelAttribute CityDto cityDto, HttpSession httpSession, Model model) {
        Long id = (Long) httpSession.getAttribute("userId");
        logger.info("addLocation method called with userId: {}, location: {}", id, cityDto);
        sessionCheckService.checkSession(sessionService, httpSession);
        try {
            Optional<User> userOptional = userService.getUserById(id);
            if (userOptional.isEmpty()) {
                logger.error("User with ID {} not found", id);
                throw new UserNotFoundException("user with this id not found");
            }
            User user = userOptional.get();
            Location location = new Location(cityDto.getName(), user, cityDto.getLat(), cityDto.getLon());
            try {
                locationService.createLocation(location);
                logger.info("Location added successfully: {}", location);
            } catch (DataIntegrityViolationException e) {
                logger.error("User with ID {} already has this location {}, e: {}", id, cityDto.getName(), e.getMessage());
                throw new LocationAlreadyAddedException("Location already added");
            }
        } catch (LocationAlreadyAddedException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("isAddPage", true); // Указываем, что это страница добавления
            return "weather";
        } catch (UserNotFoundException e) {
            return "login";
        }
        return "redirect:/weather/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteLocation(@ModelAttribute WeatherDto weatherDto, HttpSession httpSession) {
        logger.info("deleteLocation method called with userId: {}, location with id: {}", httpSession.getAttribute("userId"), weatherDto.getIdFromDatabase());
        sessionCheckService.checkSession(sessionService, httpSession);
        locationService.deleteLocationById(weatherDto.getIdFromDatabase());
        logger.info("deleteLocation: location deleted with userId: {}, location with id: {}", httpSession.getAttribute("userId"), weatherDto.getIdFromDatabase());
        return "redirect:/weather/" + httpSession.getAttribute("userId");
    }
}
