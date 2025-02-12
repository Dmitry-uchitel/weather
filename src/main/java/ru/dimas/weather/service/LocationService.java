package ru.dimas.weather.service;

import org.springframework.stereotype.Service;
import ru.dimas.weather.model.Location;
import ru.dimas.weather.repository.LocationRepository;

import java.util.Set;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Set<Location> getAllLocationByUser(Long id) {
        return (Set<Location>) locationRepository.findAllByUserId(id);
    }

    public Location createLocation(Location location){
        return locationRepository.save(location);
    }

    public boolean isExistForUser(Long userId, Location location){
        return !((Set<Location>)locationRepository.findByUserIdAndLatitudeAndLongitude(userId, location.getLatitude(), location.getLongitude())).isEmpty();
    }

}
