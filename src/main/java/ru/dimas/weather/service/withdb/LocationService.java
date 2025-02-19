package ru.dimas.weather.service.withdb;

import org.springframework.stereotype.Service;
import ru.dimas.weather.model.Location;
import ru.dimas.weather.repository.LocationRepository;

import java.math.BigDecimal;
import java.util.Set;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Iterable<Location> getAllLocationByUser(Long id) {
        return locationRepository.findAllByUserId(id);
    }

    public Location createLocation(Location location){
        return locationRepository.save(location);
    }

    public void deleteLocationById(Long locationId) {
        locationRepository.deleteById(locationId);
    }
}
