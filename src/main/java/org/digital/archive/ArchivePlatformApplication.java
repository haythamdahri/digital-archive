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

import java.util.Calendar;
import java.util.Date;

/**
 * @author Haytham DAHRI
 */
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

/*        Calendar cal = Calendar.getInstance();
        // Create a new user
        String hash = bCryptPasswordEncoder.encode("toortoor");
        Student haytham = new Student("INFORMATIQUE", 2018L, null, null, Level.MASTER);
        haytham.setPicture("default.png");
        haytham.setEmail("haytham.dahri@uit.ac.ma");
        haytham.setPassword(hash);
        haytham.setFirstName("HAYTHAM");
        haytham.setLastName("DAHRI");
        cal.set(Calendar.YEAR, 1997);
        cal.set(Calendar.MONTH, Calendar.AUGUST);
        cal.set(Calendar.DAY_OF_MONTH, 8);
        haytham.setBirthDate(cal.getTime());
        haytham.setUsername("haythamdahri");
        haytham.setAbout("Haytham dahri, étudiant en deuxième année Master Spécialisé EN Génie Logiciel Pour Le Cloud");

        Professor hadi = new Professor("INFORMATIQUE", 2006L, null, null);
        hadi.setPicture("default.png");
        hadi.setEmail("hadi.moulayyoussef@uit.ac.ma");
        hadi.setPassword(hash);
        hadi.setFirstName("HADI");
        hadi.setLastName("MOULAY YOUSSEF");
        cal.set(Calendar.YEAR, 1980);
        cal.set(Calendar.MONTH, Calendar.APRIL);
        cal.set(Calendar.DAY_OF_MONTH, 2);
        hadi.setBirthDate(cal.getTime());
        hadi.setUsername("hadi");
        hadi.setAbout("HADI MOULAY YOUSSEF, Directeur de l'école supérieure de technologie de Kénitra");

        haytham = this.studentRepository.save(haytham);
        hadi = this.professorRepository.save(hadi);

        // Set roles to the current created users
        for (RoleType roleType : RoleType.values()) {
            Role role = new Role(null, roleType, roleType.name(), null);
            this.roleService.saveRole(role);
            if (roleType == RoleType.ROLE_PROFESSOR) {
                hadi.addRole(role);
                hadi = this.professorRepository.save(hadi);
            } else if( roleType == RoleType.ROLE_STUDENT ) {
                haytham.addRole(role);
                haytham = this.studentRepository.save(haytham);
            }
        }

        // Create many users
        for (int i = 0; i < 600; i++) {
            Student etudiant = new Student("INFORMATIQUE", 2018L, null, null, Level.MASTER);
            etudiant.setPicture("default.png");
            etudiant.setEmail(ArchiveHelper.randomAlphaNumeric(15) + "@uit.ac.ma");
            etudiant.setPassword(hash);
            etudiant.setFirstName("NOM Etudiant " + i);
            etudiant.setLastName("PRENOM Etudiant " + i);
            etudiant.setBirthDate(new Date());
            etudiant.setUsername(ArchiveHelper.randomAlphaNumeric(15));
            etudiant.setAbout("Je suis " + etudiant.getFirstName() + " " + etudiant.getLastName());

            Professor professor = new Professor("INFORMATIQUE", 2006L, null, null);
            professor.setPicture("default.png");
            professor.setEmail(ArchiveHelper.randomAlphaNumeric(15) + "@uit.ac.ma");
            professor.setPassword(hash);
            professor.setFirstName("NOM Prof " + i);
            professor.setLastName("PRENOM Prof " + i);
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
        }

        System.out.println("............... DONE ...............");*/

    }
}
