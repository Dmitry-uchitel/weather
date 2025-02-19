package ru.dimas.weather.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherDto {

    private Long idFromDatabase;
    private String name;
    private double temp;
    private double feelsLike;
    private int pressure;
    private int humidity;  //Влажность
    private double windSpeed;
}
