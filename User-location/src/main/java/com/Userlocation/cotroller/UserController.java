package com.Userlocation.cotroller;

import com.Userlocation.model.UserLocation;
import com.Userlocation.serivces.UserLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

   @Autowired
   private UserLocationService userLocationService;

    @Autowired
    public UserController(UserLocationService userLocationService) {
        this.userLocationService = userLocationService;
    }


    @PostMapping("/create_data")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String createData() {


        return "Table created successfully";
    }

    @PostMapping("/update_data")
    public ResponseEntity<UserLocation> updateData(@RequestBody UserLocation userLocation, Authentication authentication)  {
        String username = authentication.getName();
        //Checking the logged user and updating user name are same are not
        if (!userLocation.getName().equals(username)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        UserLocation updatedUserLocation= new UserLocation();
        if(userLocationService.updateDate(userLocation))
            return new ResponseEntity<>(userLocation,HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/get_users/{n}")
    @PreAuthorize("hasAuthority('READER')")
    public ResponseEntity<List<UserLocation>> getUsers(@PathVariable int n) {
//        if(n==0) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return userLocationService.getUsersNearBy(n);

    }

    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Long> deleteRow(@PathVariable long id){
        return userLocationService.deleteFromTable(id);
    }
}
