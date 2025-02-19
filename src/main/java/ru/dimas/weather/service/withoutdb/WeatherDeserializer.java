package ru.dimas.weather.service.withoutdb;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.dimas.weather.DTO.WeatherDto;
import java.io.IOException;

public class WeatherDeserializer {

    public static WeatherDto deserializeWeather(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            JsonNode mainNode = jsonNode.get("main");
            if (mainNode == null) {
                throw new IllegalArgumentException("JSON не содержит узла 'main'");
            }
            WeatherDto mainWeather = new WeatherDto();
            mainWeather.setTemp(mainNode.get("temp").asDouble());
            mainWeather.setFeelsLike(mainNode.get("feels_like").asDouble());
            mainWeather.setPressure(mainNode.get("pressure").asInt());
            mainWeather.setHumidity(mainNode.get("humidity").asInt());

            JsonNode windNode = jsonNode.get("wind");
            if (windNode == null) {
                throw new IllegalArgumentException("JSON не содержит узла 'wind'");
            }
            mainWeather.setWindSpeed(windNode.get("speed").asDouble());
            return mainWeather;
        } catch (JsonMappingException e) {
            System.err.println("Ошибка десериализации: " + e.getMessage());
            return null;
        } catch (IOException e) {
            System.err.println("Ошибка чтения JSON: " + e.getMessage());
            return null;
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}