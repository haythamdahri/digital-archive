package org.digital.archive.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

@Entity
@Data
@DiscriminatorValue(value = "PROFESSOR")
@AllArgsConstructor
@NoArgsConstructor
public class Professor extends User {

    // department property
    @Column(name = "department", updatable = true, insertable = true)
    @NotNull(message = "Nom du département ne peut pas être null")
    @NotEmpty(message = "Nom du département ne peut pas être vide")
    @Size(min = 2, max = 255, message = "Nom du département non valide, veuillez ressayer avec un autre")
    private String department;

    // Recrute date
    @Column(name = "year", updatable = true, insertable = true)
    private Long startYear;

    @Column(name = "finish_year", updatable = true, insertable = true)
    private Long finishYear;

    // Archives where the professor is a jury
    // Fetch all professor archives when retrieving the current one
    // Cannot apply EAGER on two properties of the same class
    @ManyToMany(mappedBy = "professors")
    private Collection<Archive> archives;

}
