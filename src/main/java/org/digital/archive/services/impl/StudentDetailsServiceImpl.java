package org.digital.archive.services.impl;

import org.digital.archive.entities.User;
import org.digital.archive.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;


/**
 * @author Haytham DAHRI
 */
@Service
public class StudentDetailsServiceImpl implements UserDetailsService {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Following method will be invoced by Spring Security to filter the user
     * Verify credentials using basic authentication with email and password or JWT
     *
     * @param email: User Email
     * @return UserDetails
     * @throws UsernameNotFoundException: throw exception in case of unsuccessful attempt
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Retrieve security user from database using security user service instance
        // We are using email instead of username because we based our authentication on email
        User user = this.userService.getUser(email);
        if (user == null) {
            throw new UsernameNotFoundException("Authentication failed");
        }
        // In case the user exists, we need to create a collection of type GrantedAuthorities
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRole().name())));
        // After constructing the list of authorities, we will return an instance
        // of spring security user giving it username, password and authorities
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

}
