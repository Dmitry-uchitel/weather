package ru.dimas.weather.exception;

public class UserWithLoginIsNotExist extends RuntimeException{
    public UserWithLoginIsNotExist(String message) {
        super(message);
    }
}
