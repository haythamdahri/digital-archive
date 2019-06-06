package org.digital.archive.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // Username property
    @Column(name = "username", unique = true, updatable = true, insertable = true)
    @NotNull(message = "Nom d'utilisateur ne peut pas être null")
    @NotEmpty(message = "Nom d'utilisateur ne peut pas être vide")
    @Size(min = 5, max = 255, message = "Nom d'utilisateur, veuillez ressayer avec un autre")
    @Pattern(regexp = "[a-zA-Z]{2,18}", message = "Format du nom d'utilisateur non valide!")
    private String username;

    // Email property
    // User identifier
    @Id
    @Column(name = "email", unique = true, updatable = true, insertable = true)
    @NotNull(message = "Adresse email ne peut pas être null")
    @NotEmpty(message = "Adresse email ne peut pas être vide")
    @Size(min = 5, max = 255, message = "Adresse email est non valide, veuillez ressayer avec un autre")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Format d'adresse email non valide!")
    private String email;

    // Password property
    @Column(name = "password")
    @NotNull(message = "Mot de passe ne peut pas être null")
    @NotEmpty(message = "Mot de passe ne peut pas être vide")
    @Size(min = 5, max = 255, message = "Mot de passe invalide, veuillez ressayer avec un autre")
    private String password;

    // Birthdate property
    @Column(name = "birth_date")
    @Temporal(TemporalType.DATE)
    private Date birthDate;

    // Bi-directional relationship
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_email"),
            inverseJoinColumns = @JoinColumn(name = "role_role"))
    private Collection<Role> roles;

    /*
    * Custom class constructor
    */
    public User(String username, String email, String password, Date birthDate) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
    }

    /*
     * Convenient method to add new role to the current user
     */
    public void addRole(Role role) {
        if (this.roles == null) {
            this.roles = new ArrayList<>();
        }
        this.roles.add(role);
    }


}
