package ru.dimas.weather.exeption;

public class UserWithLoginIsNotExist extends RuntimeException{
    public UserWithLoginIsNotExist(String message) {
        super(message);
    }
}
