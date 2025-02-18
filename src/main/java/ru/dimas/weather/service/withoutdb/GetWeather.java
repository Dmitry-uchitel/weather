package ru.dimas.weather.service.withoutdb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.dimas.weather.DTO.CityDto;
import ru.dimas.weather.DTO.WeatherDto;
import ru.dimas.weather.exception.LocationsFromApiNotFoundException;
import ru.dimas.weather.model.Location;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class GetWeather {

    private static final Logger logger = LoggerFactory.getLogger(GetWeather.class);

    public static void main(String[] args) throws JsonProcessingException {
        CityDto cityDto = checkLocation("Angarsk", "d808a52e628b587744a56dfbc06eb60f").get(0);
        Location location = new Location();
        location.setName("Moscow");
        location.setLatitude(cityDto.getLat());
        location.setLongitude(cityDto.getLon());
        System.out.println(getWeather(location, "d808a52e628b587744a56dfbc06eb60f").getTemp());

    }

    public static List<CityDto> checkLocation(String cityName, String apiKey) {
        // URL внешнего API
        String baseUrl = "http://api.openweathermap.org/geo/1.0/direct";

        // Создаем URI с параметрами
        URI url = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("q", cityName)
                .queryParam("limit", 10)
                .queryParam("appid", apiKey)
                .build()
                .toUri();

        logger.info("Выполняется запрос к API для города: {}", cityName);
        // Создаем RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        // Выполняем GET-запрос
        String response = restTemplate.getForObject(url, String.class);
        if (response == null || response.isEmpty()) {
            logger.warn("Пустой ответ от API для города: {}", cityName);
            throw new LocationsFromApiNotFoundException("Локации по запросу не найдены");
        }
        logger.debug("Получен ответ от API: {}", response);
        // Десериализуем JSON в список объектов CityDto
        JsonMapper jsonMapper = JsonMapper.builder().build();
        List<CityDto> cities = new ArrayList<>();
        try {
            cities = jsonMapper.readValue(response, jsonMapper.getTypeFactory().constructCollectionType(List.class, CityDto.class));
        } catch (JsonProcessingException ex) {
            logger.warn("Ошибка десериализации");
//                throw new LocationsFromApiNotFoundException("Локации по запросу не найдены");
        }
        if (cities == null || cities.isEmpty()) {
            logger.warn("Список городов пуст для запроса: {}", cityName);
//                throw new LocationsFromApiNotFoundException("Локации по запросу не найдены");
        } else {
            logger.info("Найдено {} городов для запроса: {}", cities.size(), cityName);
        }// Возвращаем успешный ответ
        return cities;
    }

    public static WeatherDto getWeather(Location location, String apiKey) {
        // URL внешнего API
        String baseUrl = "https://api.openweathermap.org/data/2.5/weather";
        // Создаем URI с параметрами
        URI url = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("lat", location.getLatitude())
                .queryParam("lon", location.getLongitude())
                .queryParam("appid", apiKey)
                .queryParam("units", "metric")
                .build()
                .toUri();
        // Создаем RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        // Выполняем GET-запрос
        String response = restTemplate.getForObject(url, String.class);
        // Проверяем, что ответ не null
        if (response == null) {
            logger.warn("Пустой ответ от API для города org/data/2.5");
            return null;
        }
        // Десериализуем JSON
        WeatherDto weather = null;
        try {
            weather = WeatherDeserializer.deserializeWeather(response);
            if (weather == null) {
                logger.warn("Ошибка десериализации");
//                    throw new LocationsFromApiNotFoundException("Локации по запросу не найдены");
            }
        } catch (Exception e) {
            logger.error("Ошибка при десериализации JSON", e);
//                return ResponseEntity.status(500).body(null); // Возвращаем 500 Internal Server Error
        }
        weather.setName(location.getName());
        // Возвращаем успешный ответ
        logger.info("Получены данные о погоде: {}", weather);
        return weather;

//        } catch (Exception e) {
//            logger.error("Ошибка при получении данных о погоде", e);
//            return ResponseEntity.status(500).body(null); // Возвращаем 500 Internal Server Error
//        }
    }

}
