package ru.dimas.weather.service.withdb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.dimas.weather.model.Location;
import ru.dimas.weather.repository.LocationRepository;
import ru.dimas.weather.service.withdb.LocationService;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationService locationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllLocationByUser() {
        Long userId = 1L;
        when(locationRepository.findAllByUserId(userId)).thenReturn(Collections.emptyList());
        Iterable<Location> locations = locationService.getAllLocationByUser(userId);
        assertNotNull(locations);
        verify(locationRepository, times(1)).findAllByUserId(userId);
    }

    @Test
    void testCreateLocation() {
        Location location = new Location();
        location.setName("Test Location");
        when(locationRepository.save(location)).thenReturn(location);
        Location savedLocation = locationService.createLocation(location);
        assertNotNull(savedLocation);
        assertEquals("Test Location", savedLocation.getName());
        verify(locationRepository, times(1)).save(location);
    }

    @Test
    void testDeleteLocationById() {
        Long locationId = 1L;
        locationService.deleteLocationById(locationId);
        verify(locationRepository, times(1)).deleteById(locationId);
    }
}