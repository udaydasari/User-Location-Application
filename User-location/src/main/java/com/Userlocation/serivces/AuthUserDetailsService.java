package com.Userlocation.serivces;

import com.Userlocation.model.UserRole;
import com.Userlocation.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class AuthUserDetailsService implements UserDetailsService {
    @Autowired
    private UserDetailsRepository userDetailsRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            com.Userlocation.model.UserDetails userDetails=userDetailsRepository.findByUserName(username);

            if(userDetails.equals(null))
                throw new UsernameNotFoundException(username+" is not found");

          /*  UserDetails GroupuserDetails= new UserDetails() {
                List<GrantedAuthority> authorithes= Arrays.asList(UserRole.values().toString())
                        .stream()
                        .map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    return authorithes;
                }

                @Override
                public String getPassword() {
                    return userDetails.getPassword();
                }

                @Override
                public String getUsername() {
                    return userDetails.getUserName();
                }

                @Override
                public boolean isAccountNonExpired() {
                    return true;
                }

                @Override
                public boolean isAccountNonLocked() {
                    return true;
                }

                @Override
                public boolean isCredentialsNonExpired() {
                    return true;
                }

                @Override
                public boolean isEnabled() {
                    return true;
                }
            };*/

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userDetails.getUserRole().name()));


//                Arrays.stream(UserRole.values())
//                .map(role -> new SimpleGrantedAuthority(role.toString()))
//                .collect(Collectors.toList());

        return User.builder()
                .username(userDetails.getUserName())
                .password(userDetails.getPassword())
                .authorities(authorities)
                .build();
    }
}
