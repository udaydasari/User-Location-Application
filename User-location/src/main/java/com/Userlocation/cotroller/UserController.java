package com.Userlocation.cotroller;

import com.Userlocation.model.UserDetails;
import com.Userlocation.model.UserLocation;
import com.Userlocation.repository.UserDetailsRepository;
import com.Userlocation.repository.UserLocationRepository;
import com.Userlocation.serivces.UserLocationService;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.geom.impl.PackedCoordinateSequenceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.locationtech.jts.operation.distance.DistanceOp;

import org.locationtech.jts.geom.Point;

import javax.persistence.EntityNotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserController {

   @Autowired
   private UserLocationService userLocationService;


    @PostMapping("/create_data")
    public String createData() {

//        userRepository.createTable();
        return "Table created successfully";
    }

    @PostMapping("/update_data")
    public ResponseEntity<UserLocation> updateData(@RequestBody UserLocation userLocation)  {

        UserLocation updatedUserLocation= new UserLocation();
        if(userLocationService.updateDate(userLocation))
            return new ResponseEntity<>(userLocation,HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/get_users/{n}")
    public ResponseEntity<List<UserLocation>> getUsers(@PathVariable int n) {

        return userLocationService.getUsersNearBy(n);
//       ResponseEntity <List<UserLocation>>nearestUsers=userLocationService.getUsersNearBy(n);
//        if(nearestUsers.getStatusCode().is4xxClientError())
//            return new ResponseEntity<List<UserLocation>>((List<UserLocation>) nearestUsers,HttpStatus.BAD_REQUEST);
//
//        return new ResponseEntity<List<UserLocation>>((List<UserLocation>) nearestUsers,HttpStatus.OK);
    }

}
