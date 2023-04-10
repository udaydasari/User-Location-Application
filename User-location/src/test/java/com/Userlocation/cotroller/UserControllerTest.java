package com.Userlocation.cotroller;

import com.Userlocation.model.UserLocation;
import com.Userlocation.serivces.UserLocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserLocationService userLocationService;

    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userLocationService);
    }

    @Test
    void testCreateData() {
        String result = userController.createData();
        assertEquals("Table created successfully", result);
    }

    @Test
    void testUpdateDataAuthorized() {
        UserLocation userLocation = new UserLocation();
        userLocation.setName("test");
        Authentication authentication = new UsernamePasswordAuthenticationToken("test", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userLocationService.updateDate(any(UserLocation.class))).thenReturn(true);
        ResponseEntity<UserLocation> response = userController.updateData(userLocation, authentication);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testUpdateDataUnauthorized() {
        UserLocation userLocation = new UserLocation();
        userLocation.setName("test");
        Authentication authentication = new UsernamePasswordAuthenticationToken("differentuser", "password");
        ResponseEntity<UserLocation> response = userController.updateData(userLocation, authentication);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testGetUsers() {
        int n = 5;
        List<UserLocation> users = new ArrayList<>();
        when(userLocationService.getUsersNearBy(n)).thenReturn(new ResponseEntity<>(users, HttpStatus.OK));
        ResponseEntity<List<UserLocation>> response = userController.getUsers(n);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    void testDeleteRow() {
        long id = 123;
        when(userLocationService.deleteFromTable(id)).thenReturn(new ResponseEntity<>(id, HttpStatus.OK));
        ResponseEntity<Long> response = userController.deleteRow(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(id, response.getBody());
    }
}
