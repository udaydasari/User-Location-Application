package com.Userlocation.cotroller;

import com.Userlocation.model.UserLocation;
import com.Userlocation.repository.UserRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.geom.impl.PackedCoordinateSequenceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.web.bind.annotation.*;
import org.locationtech.jts.operation.distance.DistanceOp;

import org.locationtech.jts.geom.Point;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create_data")
    public String createData() {

//        userRepository.createTable();
        return "Table created successfully";
    }

    @PostMapping("/update_data")
    public UserLocation updateData(@RequestBody UserLocation userLocation) {
        return userRepository.save(userLocation);
    }

    @GetMapping("/get_users/{n}")
    public List<UserLocation> getUsers(@PathVariable int n) {
        List<UserLocation> allUsers = userRepository.findAll();
        //Point origin = new Point(0, 0);
        List<UserLocation> nearestUsers = allUsers.stream()
                .filter(user -> user.getLatitude() != 0 && user.getLongitude() != 0)
                .map(user -> {
                    GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326, new PackedCoordinateSequenceFactory());

                    double latitude = user.getLatitude();
                    double longitude = user.getLongitude();
                    Coordinate coordinate1 = new Coordinate(longitude, latitude);
                    Point userpoint= factory.createPoint(coordinate1);
                    Point origin = factory.createPoint(new Coordinate(0,0));

                    DistanceOp distance = new DistanceOp(origin, userpoint);
                    user.setDistance(distance.distance());
                    return user;
                })
                .sorted(Comparator.comparingDouble(UserLocation::getDistance))
                .limit(n)
                .collect(Collectors.toList());
        return nearestUsers;
    }

}
