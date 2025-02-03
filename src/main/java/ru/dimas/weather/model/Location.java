package ru.dimas.weather.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@ToString(includeFieldNames=true)
public class Location {
    private Long id;
    private String name;
    private Long userId;
    private BigDecimal latitude;
    private BigDecimal longitude;

}
