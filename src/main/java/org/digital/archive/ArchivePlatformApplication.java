package org.digital.archive;

import org.digital.archive.entities.*;
import org.digital.archive.repositories.ProfessorRepository;
import org.digital.archive.repositories.StudentRepository;
import org.digital.archive.services.RoleService;
import org.digital.archive.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;

@SpringBootApplication
public class ArchivePlatformApplication implements CommandLineRunner {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public static void main(String[] args) {
        SpringApplication.run(ArchivePlatformApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // Create a new user
        String hash = bCryptPasswordEncoder.encode("toortoor");
        /*Student haytham = new Student("INFORMATIQUE", 2018L, null, null, Level.MASTER);
        haytham.setPicture("/src/image.png");
        haytham.setEmail("haytham.dahri@gmail.com");
        haytham.setPassword(hash);
        haytham.setFirstName("HAYTHAM");
        haytham.setLastName("DAHRI");
        haytham.setBirthDate(new Date(1997, 8, 26));
        haytham.setUsername("haythamdahri");

        Professor housni = new Professor("INFORMATIQUE", 2006L, null, null);
        housni.setPicture("/src/image.png");
        housni.setEmail("khalid.housni@gmail.com");
        housni.setPassword(hash);
        housni.setFirstName("KHALID");
        housni.setLastName("HOUSNI");
        housni.setBirthDate(new Date(1980, 4, 2));
        housni.setUsername("khalidhousni");

        haytham = this.studentRepository.save(haytham);
        housni = this.professorRepository.save(housni);

        // Set roles to the current created users
        for (RoleType roleType : RoleType.values()) {
            Role role = new Role(null, roleType, roleType.name(), null);
            this.roleService.saveRole(role);
            if (roleType == RoleType.ROLE_PROFESSOR) {
                housni.addRole(role);
                housni = this.professorRepository.save(housni);
            } else if( roleType == RoleType.ROLE_STUDENT ) {
                haytham.addRole(role);
                haytham = this.studentRepository.save(haytham);
            }
        }*/
        System.out.println("Hash => " + hash);
    }
}
