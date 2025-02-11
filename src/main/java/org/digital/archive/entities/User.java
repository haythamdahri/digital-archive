package org.digital.archive.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * @author Haytham DAHRI
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "users")
@DiscriminatorColumn(name = "userType")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // Username property
    @Column(name = "username", unique = true, updatable = true, insertable = true)
    @NotNull(message = "Nom utilisateur ne peut pas être null!")
    @NotEmpty(message = "Nom utilisateur ne peut pas être vide!")
    @Size(min = 5, max = 255, message = "Nom utilisateur non valide, veuillez ressayer avec un autre!")
    @Pattern(regexp = "^[a-zA-Z0-9]([._](?![._])|[a-zA-Z0-9]){6,18}[a-zA-Z0-9]${2,18}", message = "Format du nom utilisateur non valide!")
    private String username;

    // First name property
    @Column(name = "first_name", updatable = true, insertable = true)
    @NotNull(message = "Prénom ne peut pas être null!")
    @NotEmpty(message = "Prénom ne peut pas être vide!")
    @Size(min = 2, max = 255, message = "Prénom non valide, veuillez ressayer avec un autre!")
    private String firstName;

    // Username property
    @Column(name = "last_name", updatable = true, insertable = true)
    @NotNull(message = "Nom ne peut pas être null!")
    @NotEmpty(message = "Nom ne peut pas être vide!")
    @Size(min = 2, max = 255, message = "Nom non valide, veuillez ressayer avec un autre!")
    private String lastName;

    // Picture property
    @Column(name = "picture")
    @NotNull(message = "Image ne peut pas être null!")
    @NotEmpty(message = "Image ne peut pas être vide!")
    @Size(min = 5, max = 8500, message = "Image non valide, veuillez ressayer avec une autre!")
    private String picture;

    // Email property
    // User identifier
    @Column(name = "email", unique = true, updatable = true, insertable = true)
    @NotNull(message = "Adresse email ne peut pas être null!")
    @NotEmpty(message = "Adresse email ne peut pas être vide!")
    @Size(min = 5, max = 255, message = "Adresse email est non valide, veuillez ressayer avec un autre!")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Format adresse email non valide!")
    private String email;

    // Password property
    @Column(name = "password")
    @NotNull(message = "Mot de passe ne peut pas être null!")
    @NotEmpty(message = "Mot de passe ne peut pas être vide!")
    @Size(min = 5, max = 255, message = "Mot de passe non valide, veuillez ressayer avec un autre!")
    private String password;

    // About property
    @Column(name = "about")
    @NotNull(message = "La description ne peut pas être null!")
    @NotEmpty(message = "La description ne peut pas être vide!")
    @Size(min = 10, max = 5000, message = "La description est non valide, veuillez ressayer avec un autre!")
    private String about;

    // Birthdate property
    @Column(name = "birth_date")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
    private Date birthDate;

    // Bi-directional relationship
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_role"))
    private Collection<Role> roles;

    /**
     * Custom class constructor
     *
     * @param username:  user username
     * @param email:     user email
     * @param password:  user password
     * @param birthDate: user birth date
     */
    public User(String username, String email, String password, Date birthDate) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
    }

    // User age
    public Integer getAge() {
        Instant instant = Instant.ofEpochMilli(this.birthDate.getTime());
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        LocalDate localDate = localDateTime.toLocalDate();
        return Period.between(localDate, LocalDate.now()).getYears();
    }

    /**
     * User full name
     *
     * @return String
     */
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }


    /**
     * Convenient method to add new role to the current user
     *
     * @param role: Role
     */
    public void addRole(Role role) {
        if (this.roles == null) {
            this.roles = new ArrayList<>();
        }
        this.roles.add(role);
    }


}
