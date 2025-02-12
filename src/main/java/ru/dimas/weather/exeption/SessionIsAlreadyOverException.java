package ru.dimas.weather.exeption;

public class SessionIsAlreadyOverException extends RuntimeException{
    public SessionIsAlreadyOverException(String message) {
        super(message);
    }
}
