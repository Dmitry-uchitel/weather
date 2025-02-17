package ru.dimas.weather.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.http.HttpEntity;
import org.springframework.ui.Model;
import ru.dimas.weather.controller.WeatherController;
import ru.dimas.weather.deserialization.WeatherMain;
import ru.dimas.weather.model.Location;
import ru.dimas.weather.model.User;
import ru.dimas.weather.service.LocationService;
import ru.dimas.weather.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

public class GetWeatherForUser {

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);



    public static void getAllWeatherForUser(Model model, Long id, UserService userService, LocationService locationService, String apiKey){
        logger.info("metod getAllWeather for User with ID {} start", id);
        Optional<User> userOptional = userService.getUserById(id);
        if (userOptional.isEmpty()) {
            logger.error("User with ID {} not found", id);
            throw new IllegalArgumentException("User not found");
        }

        User user = userOptional.get();
        Iterable<Location> locationSet = locationService.getAllLocationByUser(user.getId());
        List<WeatherMain> weatherData = new ArrayList<>();

        if (locationSet.iterator().hasNext()) {
            for (Location location : locationSet) {
                HttpEntity<WeatherMain> weatherMain1 = GetWeather.getWeather(location, apiKey);
                WeatherMain weatherMain = weatherMain1.getBody();
                weatherMain.setIdFromDatabase(location.getId());
                weatherData.add(weatherMain);
            }
        }
        logger.info("Adding to model: weatherData size = {}", weatherData.size());
            model.addAttribute("weatherData", weatherData);
            model.addAttribute("userLogin", user.getLogin());
        logger.info("metod getAllWeather for User with ID {} end", id);
    }
}
