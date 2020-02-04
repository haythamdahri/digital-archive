package org.digital.archive;

import org.digital.archive.entities.*;
import org.digital.archive.repositories.ProfessorRepository;
import org.digital.archive.repositories.StudentRepository;
import org.digital.archive.services.RoleService;
import org.digital.archive.services.UserService;
import org.digital.archive.utils.ArchiveHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;
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
        haytham.setAbout("Haytham dahri, étudiant en premiere année Master Spécialisé EN Génie Logiciel Pour Le Cloud");

        Professor housni = new Professor("INFORMATIQUE", 2006L, null, null);
        housni.setPicture("/src/image.png");
        housni.setEmail("khalid.housni@gmail.com");
        housni.setPassword(hash);
        housni.setFirstName("KHALID");
        housni.setLastName("HOUSNI");
        housni.setBirthDate(new Date(1980, 4, 2));
        housni.setUsername("khalidhousni");
        housni.setAbout("Khalid Housni, Professeur Chercheur et président de l'université Ibn Tofail et membre de l'union international des technologies AI");

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
        }

        // Create many users
        for (int i = 0; i < 600; i++) {
            Student etudiant = new Student("INFORMATIQUE", 2018L, null, null, Level.MASTER);
            etudiant.setPicture("1.jpg");
            etudiant.setEmail(ArchiveHelper.randomAlphaNumeric(15) + "@gmail.com");
            etudiant.setPassword(hash);
            etudiant.setFirstName(i<=300 ? "AAAAA" : "BBBBBB");
            etudiant.setLastName(i<=300 ? "CCCCC" : "DDDDDD");
            etudiant.setBirthDate(new Date());
            etudiant.setUsername(ArchiveHelper.randomAlphaNumeric(15));
            etudiant.setAbout("Je suis " + etudiant.getFirstName() + " " + etudiant.getLastName());

            Professor professor = new Professor("INFORMATIQUE", 2006L, null, null);
            professor.setPicture("1.jpg");
            professor.setEmail(ArchiveHelper.randomAlphaNumeric(15) + "@gmail.com");
            professor.setPassword(hash);
            professor.setFirstName(i<=300 ? "AAAAA" : "BBBBBB");
            professor.setLastName(i<=300 ? "CCCCC" : "DDDDDD");
            professor.setBirthDate(new Date());
            professor.setUsername(ArchiveHelper.randomAlphaNumeric(15));
            professor.setAbout(professor.getFullName() + " Professeur Chercheur");

            etudiant = this.studentRepository.save(etudiant);
            professor = this.professorRepository.save(professor);

            // Set roles to the current created users
            for (RoleType roleType : RoleType.values()) {
                Role role = new Role(null, roleType, roleType.name(), null);
                this.roleService.saveRole(role);
                if (roleType == RoleType.ROLE_PROFESSOR) {
                    professor.addRole(role);
                    professor = this.professorRepository.save(professor);
                } else if (roleType == RoleType.ROLE_STUDENT) {
                    etudiant.addRole(role);
                    etudiant = this.studentRepository.save(etudiant);
                }
            }
        } */

    }
}
