package com.Userlocation.repository;

import com.Userlocation.model.UserLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserLocation, Long> {

//    void createTable();
}

