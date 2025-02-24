package ru.dimas.weather.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.math.BigDecimal;

@Getter
@Setter
@ToString(exclude = {"local_names"})
public class CityDto {

    private String name;
    @JsonIgnore
    private String local_names;
    private String state;
    private BigDecimal lat;
    private BigDecimal lon;
    private String country;
}
