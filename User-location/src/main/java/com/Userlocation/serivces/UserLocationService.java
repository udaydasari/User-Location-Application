package com.Userlocation.serivces;

import com.Userlocation.model.UserLocation;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserLocationService {
    boolean updateDate(UserLocation userLocation);

    ResponseEntity<List<UserLocation>> getUsersNearBy(int n);

    ResponseEntity<Long> deleteFromTable(long id);
}
