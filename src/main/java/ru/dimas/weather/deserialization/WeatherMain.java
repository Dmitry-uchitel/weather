package ru.dimas.weather.deserialization;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherMain {

    private Long idFromDatabase;
    private String name;
    private double temp;
    private double feelsLike;
    private int pressure;
    private int humidity;  //Влажность
    private double windSpeed;

}
