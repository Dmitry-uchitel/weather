package ru.dimas.weather.exeption;

public class LocationAlreadyAddedException extends RuntimeException{
    public LocationAlreadyAddedException(String message) {
        super(message);
    }
}
