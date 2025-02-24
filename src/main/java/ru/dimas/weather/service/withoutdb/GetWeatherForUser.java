package ru.dimas.weather.service.withoutdb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.dimas.weather.DTO.WeatherDto;
import ru.dimas.weather.controller.WeatherController;
import ru.dimas.weather.model.Location;
import ru.dimas.weather.model.User;
import ru.dimas.weather.service.withdb.LocationService;
import ru.dimas.weather.service.withdb.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GetWeatherForUser {

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);


    private final UserService userService;
    private final LocationService locationService;
    private final GetWeather getWeather;


    @Value("${openweathermap.api.key}")
    private String apiKey; // Загружается из application.properties

    public GetWeatherForUser(UserService userService, LocationService locationService, GetWeather getWeather) {
        this.userService = userService;
        this.locationService = locationService;
        this.getWeather=getWeather;
    }

    public void getAllWeatherForUser(Model model, Long userId) {
        logger.info("metod getAllWeather for User with ID {} start", userId);
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isEmpty()) {
            logger.error("User with ID {} not found", userId);
            throw new IllegalArgumentException("User not found");
        }

        User user = userOptional.get();
        Iterable<Location> locationSet = locationService.getAllLocationByUser(user.getId());
        List<WeatherDto> weatherList = new ArrayList<>();

        if (locationSet.iterator().hasNext()) {
            for (Location location : locationSet) {
                WeatherDto weatherDto = getWeather.getWeather(location, apiKey);

                weatherDto.setIdFromDatabase(location.getId());
                weatherList.add(weatherDto);
            }
        }
        logger.info("Adding to model: weatherData size = {}", weatherList.size());
        model.addAttribute("weatherList", weatherList);
        model.addAttribute("userLogin", user.getLogin());
        logger.info("metod getAllWeather for User with ID {} end", userId);
    }
}
