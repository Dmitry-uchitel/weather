package ru.dimas.weather.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@ToString(includeFieldNames=true)
public class Session {
    private String id;
    private Long userId;
    private LocalDateTime ExpiresAt;


}
