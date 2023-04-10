package com.Userlocation.serivces;

import com.Userlocation.model.UserDetails;
import com.Userlocation.model.UserLocation;
import com.Userlocation.repository.UserDetailsRepository;
import com.Userlocation.repository.UserLocationRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.geom.impl.PackedCoordinateSequenceFactory;
import org.locationtech.jts.operation.distance.DistanceOp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserLocationServiceImpl implements UserLocationService {
    @Autowired
    private UserLocationRepository userLocationRepository;
    @Autowired
    private UserDetailsRepository userDetailsRepository;



    @Override
    public boolean updateDate(UserLocation userLocation) {
        UserLocation existingUserLocation = userLocationRepository.findByName(userLocation.getName());
        if (existingUserLocation == null) {
            return false;
        }
        existingUserLocation.setLongitude(userLocation.getLongitude());
        existingUserLocation.setLatitude(userLocation.getLatitude());
        UserLocation updatedUserLocation = userLocationRepository.save(existingUserLocation);
        return ResponseEntity.ok(updatedUserLocation).hasBody();
    }

    @Override
    public ResponseEntity<List<UserLocation>> getUsersNearBy(int n) {
        List<UserLocation> allUsers = userLocationRepository.findAll();
        List<UserLocation> nearestUsers = new ArrayList<>();
        if(allUsers.size() >=n) {
            nearestUsers = allUsers.stream()
                    .filter(user -> user.getLatitude() != 0 && user.getLongitude() != 0)
                    .map(user -> {
                        GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326, new PackedCoordinateSequenceFactory());

                        double latitude = user.getLatitude();
                        double longitude = user.getLongitude();
                        Coordinate coordinate1 = new Coordinate(longitude, latitude);
                        Point userpoint = factory.createPoint(coordinate1);
                        Point origin = factory.createPoint(new Coordinate(0, 0));

                        DistanceOp distance = new DistanceOp(origin, userpoint);
                        user.setDistance(distance.distance());
                        return user;
                    })
                    .sorted(Comparator.comparingDouble(UserLocation::getDistance))
                    .limit(n)
                    .collect(Collectors.toList());

            return new ResponseEntity<>(nearestUsers, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(allUsers,HttpStatus.CONFLICT);
    }

    @Override
    public ResponseEntity<Long> deleteFromTable(long id) {
        userDetailsRepository.delete(userDetailsRepository.findById(id)
                .orElseThrow(()->new RuntimeException("No customer found to delete")));
        return new ResponseEntity<>(id,HttpStatus.OK);

    }
}
