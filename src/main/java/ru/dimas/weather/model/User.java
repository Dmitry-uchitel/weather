package ru.dimas.weather.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString(includeFieldNames=true, exclude = {"locationSet","sessionSet"})
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String login;
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Setter(AccessLevel.NONE) // Запрещаем прямое изменение коллекции через сеттер
    @JsonManagedReference
    @JsonIgnore
    private Set<Location> locationSet = new HashSet<>();

    public void addLocation(Location location) {
        locationSet.add(location);
        location.setUser(this); // Установка обратной связи
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Setter(AccessLevel.NONE) // Запрещаем прямое изменение коллекции через сеттер
    @JsonManagedReference
    @JsonIgnore
    private Set<Session> sessionSet = new HashSet<>();

    public void addSession(Session session) {
        sessionSet.add(session);
        session.setUser(this); // Установка обратной связи
    }
}
