package ru.dimas.weather.exception;

public class LocationsFromApiNotFoundException extends RuntimeException{
    public LocationsFromApiNotFoundException(String message) {
        super(message);
    }
}
