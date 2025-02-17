package ru.dimas.weather.exception;

public class SessionIsAlreadyOverException extends RuntimeException{
    public SessionIsAlreadyOverException(String message) {
        super(message);
    }
}
