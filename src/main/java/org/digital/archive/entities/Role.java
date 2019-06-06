package org.digital.archive.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id")
    private Long id;

    // Role identifier
    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "role", unique = true)
    private RoleType role;

    @Column(name = "description", length = 5000, columnDefinition = "TEXT", updatable = true, insertable = true)
    private String description;

    // Bi-directional relationship
    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    private List<User> users;

    /*
    * Custom class constructor
    */
    public Role(RoleType role, String description) {
        this.role = role;
        this.description = description;
    }

    /*
     * Convenient method to add new user to the current role users list
     */
    public void addUser(User user) {
        if (this.users == null) {
            this.users = new ArrayList<>();
        }
        this.users.add(user);
    }

}
