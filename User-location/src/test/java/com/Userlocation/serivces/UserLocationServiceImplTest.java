package com.Userlocation.serivces;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import com.Userlocation.model.UserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.Assertions;
import com.Userlocation.model.UserLocation;
import com.Userlocation.repository.UserDetailsRepository;
import com.Userlocation.repository.UserLocationRepository;

@ExtendWith(MockitoExtension.class)
class UserLocationServiceImplTest {

    @Mock
    private UserLocationRepository userLocationRepository;

    @Mock
    private UserDetailsRepository userDetailsRepository;

    @InjectMocks
    private UserLocationServiceImpl userLocationService;

    private UserLocation userLocation1;
    private UserLocation userLocation2;
    private UserLocation userLocation3;

    @BeforeEach
    void setUp() throws Exception {
        userLocation1 = new UserLocation("user1", 42.0, -71.0);
        userLocation2 = new UserLocation("user2", 42.3, -71.2);
        userLocation3 = new UserLocation("user3", 42.1, -71.1);
    }

    @Test
    void testUpdateDate() {
        when(userLocationRepository.findByName("user1")).thenReturn(userLocation1);
        when(userLocationRepository.save(userLocation1)).thenReturn(userLocation1);

        userLocation1.setLatitude(42.5);
        boolean result = userLocationService.updateDate(userLocation1);
        assertThat(result).isTrue();
        assertThat(userLocation1.getLatitude()).isEqualTo(42.5);
    }

    @Test
    void testUpdateDateWhenUserNotFound() {
        when(userLocationRepository.findByName("user1")).thenReturn(null);

        boolean result = userLocationService.updateDate(userLocation1);
        assertThat(result).isFalse();
    }

    @Test
    void testGetUsersNearBy() {
        List<UserLocation> allUsers = new ArrayList<>();
        allUsers.add(userLocation1);
        allUsers.add(userLocation2);
        allUsers.add(userLocation3);
        when(userLocationRepository.findAll()).thenReturn(allUsers);

        ResponseEntity<List<UserLocation>> response = userLocationService.getUsersNearBy(2);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(2);
        assertThat(response.getBody().get(0).getName()).isEqualTo("user1");
        assertThat(response.getBody().get(1).getName()).isEqualTo("user3");
    }

    @Test
    void testGetUsersNearByWhenNotEnoughUsers() {
        List<UserLocation> allUsers = new ArrayList<>();
        allUsers.add(userLocation1);
        allUsers.add(userLocation2);
        allUsers.add(userLocation3);
        when(userLocationRepository.findAll()).thenReturn(allUsers);

        ResponseEntity<List<UserLocation>> response = userLocationService.getUsersNearBy(5);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().size()).isEqualTo(3);
    }









    @Test
    public void testUpdateDate2() {
        // Given
        UserLocation existingUserLocation = new UserLocation("John", 12.34, 56.78);
        UserLocation userLocationToUpdate = new UserLocation("John", 23.45, 67.89);
        Mockito.when(userLocationRepository.findByName("John")).thenReturn(existingUserLocation);
        Mockito.when(userLocationRepository.save(existingUserLocation)).thenReturn(userLocationToUpdate);

        // When
        boolean result = userLocationService.updateDate(userLocationToUpdate);

        // Then
        Assertions.assertTrue(result);
        Mockito.verify(userLocationRepository, Mockito.times(1)).findByName("John");
        Mockito.verify(userLocationRepository, Mockito.times(1)).save(existingUserLocation);
    }

    @Test
    public void testGetUsersNearBy2() {
        // Given
        List<UserLocation> allUsers = new ArrayList<>();
        allUsers.add(new UserLocation("John", 10.0, 20.0));
        allUsers.add(new UserLocation("Jane", 30.0, 40.0));
        allUsers.add(new UserLocation("Jack", 50.0, 60.0));
        Mockito.when(userLocationRepository.findAll()).thenReturn(allUsers);

        // When
        ResponseEntity<List<UserLocation>> result1 = userLocationService.getUsersNearBy(2);
        ResponseEntity<List<UserLocation>> result2 = userLocationService.getUsersNearBy(4);

        // Then
        Assertions.assertEquals(HttpStatus.OK, result1.getStatusCode());
        Assertions.assertEquals(2, result1.getBody().size());

        Assertions.assertEquals(HttpStatus.CONFLICT, result2.getStatusCode());
        Assertions.assertEquals(3, result2.getBody().size());

        Mockito.verify(userLocationRepository, Mockito.times(2)).findAll();
    }

    @Test
    public void testDeleteFromTable() {
        // Given
        long id = 123;
        Optional<UserDetails> userDetails = Optional.of(new UserDetails(id, "John", "Doe"));
        Mockito.when(userDetailsRepository.findById(id)).thenReturn(userDetails);

        // When
        ResponseEntity<Long> result = userLocationService.deleteFromTable(id);

        // Then
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(id, result.getBody().longValue());
        Mockito.verify(userDetailsRepository, Mockito.times(1)).delete(userDetails.get());
        Mockito.verify(userDetailsRepository, Mockito.times(1)).findById(id);
    }
}
