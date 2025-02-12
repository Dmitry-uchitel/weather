package ru.dimas.weather.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.dimas.weather.deserialization.City;
import ru.dimas.weather.deserialization.WeatherDeserializer;
import ru.dimas.weather.deserialization.WeatherMain;
import ru.dimas.weather.model.Location;

import java.io.IOException;
import java.net.URI;
import java.util.List;

public class GetWeather {

    private static final Logger logger = LoggerFactory.getLogger(GetWeather.class);

    public static void main(String[] args) throws JsonProcessingException {
        City city = checkLocation("Angarsk", "d808a52e628b587744a56dfbc06eb60f").getBody().get(0);
        Location location = new Location();
        location.setName("Moscow");
        location.setLatitude(city.getLat());
        location.setLongitude(city.getLon());
        System.out.println(getWeather(location, "d808a52e628b587744a56dfbc06eb60f").getBody().getTemp());

    }

    public static ResponseEntity<List<City>> checkLocation(String cityName, String apiKey) {
        try {
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
                return ResponseEntity.noContent().build(); // Возвращаем 204 No Content, если ответ пустой
            }

            logger.debug("Получен ответ от API: {}", response);

            // Десериализуем JSON в список объектов City
            JsonMapper jsonMapper = JsonMapper.builder().build();
            List<City> cities = jsonMapper.readValue(response, jsonMapper.getTypeFactory().constructCollectionType(List.class, City.class));

            if (cities == null || cities.isEmpty()) {
                logger.warn("Список городов пуст для запроса: {}", cityName);
                return ResponseEntity.noContent().build(); // Возвращаем 204 No Content, если список городов пуст
            }

            logger.info("Найдено {} городов для запроса: {}", cities.size(), cityName);

            // Возвращаем успешный ответ
            return ResponseEntity.ok(cities);

        } catch (JsonMappingException e) {
            logger.error("Ошибка десериализации JSON для города {}: {}", cityName, e.getMessage());
            return ResponseEntity.badRequest().body(null); // Возвращаем 400 Bad Request при ошибке десериализации
        } catch (IOException e) {
            logger.error("Ошибка чтения JSON для города {}: {}", cityName, e.getMessage());
            return ResponseEntity.status(500).body(null); // Возвращаем 500 Internal Server Error при ошибках чтения
        } catch (Exception e) {
            logger.error("Произошла неожиданная ошибка для города {}: {}", cityName, e.getMessage(), e);
            return ResponseEntity.status(500).body(null); // Возвращаем 500 Internal Server Error при других исключениях
        }
    }

    public static ResponseEntity<WeatherMain> getWeather(Location location, String apiKey) {
        try {
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
                logger.warn("Пустой ответ от API");
                return ResponseEntity.noContent().build(); // Возвращаем 204 No Content
            }

            // Десериализуем JSON
            WeatherMain weather = null;
            try {
                weather = WeatherDeserializer.deserializeWeather(response);
                if (weather == null) {
                    logger.error("Не удалось десериализовать JSON");
                    return ResponseEntity.badRequest().body(null); // Возвращаем 400 Bad Request
                }
            } catch (Exception e) {
                logger.error("Ошибка при десериализации JSON", e);
                return ResponseEntity.status(500).body(null); // Возвращаем 500 Internal Server Error
            }
            weather.setName(location.getName());
            // Возвращаем успешный ответ
            logger.info("Получены данные о погоде: {}", weather);
            return ResponseEntity.ok(weather);

        } catch (Exception e) {
            logger.error("Ошибка при получении данных о погоде", e);
            return ResponseEntity.status(500).body(null); // Возвращаем 500 Internal Server Error
        }
    }

}
