package com.Userlocation.model;

import lombok.*;

import javax.persistence.*;
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userId;

    @Column(unique = true)
    private String userName;

    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_location_id", referencedColumnName = "id")
    private UserLocation userLocation;

    public UserDetails(long id, String john, String doe) {
        this.userId= id;
        this.userName=john;
        this.password=doe;


    }
}
