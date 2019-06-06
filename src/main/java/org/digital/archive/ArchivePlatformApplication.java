package org.digital.archive;

import org.digital.archive.entities.Role;
import org.digital.archive.entities.RoleType;
import org.digital.archive.entities.User;
import org.digital.archive.services.RoleService;
import org.digital.archive.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;
import java.util.stream.Stream;

@SpringBootApplication
public class ArchivePlatformApplication implements CommandLineRunner {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public static void main(String[] args) {
        SpringApplication.run(ArchivePlatformApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // Create a new user
        String hash = bCryptPasswordEncoder.encode("toortoor");
//        User user = new User(null, "haytham", "haytham.dahri@gmail.com", hash, new Date(1997, 8, 26), null);
//        user = this.userService.saveUser(user);
//
//        // Set roles to the current created user
//        for( RoleType roleType : RoleType.values() ){
//            Role role = new Role(null, roleType, roleType.name(), null);
//            this.roleService.saveRole(role);
//            user.addRole(role);
//        }
//        this.userService.saveUser(user);
//        for( Role role : this.roleService.getRoles() ) {
//            user.addRole(role);
//            System.out.println(role.getRoleType().name());
//        }
//        this.userService.saveUser(user);


        System.out.println("Hash => " + hash);
    }
}
