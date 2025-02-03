package ru.dimas.weather.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(includeFieldNames=true)
public class User {
    private long id;
    private String login;
    private String password;


}
