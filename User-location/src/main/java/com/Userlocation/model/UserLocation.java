package com.Userlocation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;


@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLocation {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @JsonIgnore
        private Long id;
        @Column(unique = true)
        private String name;

        private double latitude;

        private double longitude;

        @Transient
        private double distance;
        @JsonIgnore
        @OneToOne(mappedBy = "userLocation")
        private UserDetails userDetails;




}
