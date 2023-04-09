package com.Userlocation.repository;

import com.Userlocation.model.UserLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLocationRepository extends JpaRepository<UserLocation, Long> {


    UserLocation findByName(String name);
}

