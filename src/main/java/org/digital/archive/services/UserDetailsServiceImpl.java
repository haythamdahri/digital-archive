package org.digital.archive.services;

import org.digital.archive.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

/*
* Define users login mechanism
*/

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /*
     * @Following method will be invoced by Spring Security to filter the user
     * to verify the credientials using basic authentication with email and password or with JWT
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Retrieve security user from database using security user service instance
        // We are using email instead of username because we based our authentication on email
        User user = this.userService.getUser(email);
        if( user == null ) {
            throw new UsernameNotFoundException("Authentication failed");
        }
        // In case the user exists, we need to create a collection of type GrantedAuthorities
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getRole().name()));
        });
        // After constructing the list of authorities, we will return an instance
        // of spring security user giving it username, password and authorities
        System.out.println("User password => " + user.getPassword());
        System.out.println(this.bCryptPasswordEncoder.matches("toortoor", user.getPassword()));


        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

}
