package org.digital.archive.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Professor extends User {

    // department property
    @Column(name = "department", updatable = true, insertable = true)
    @NotNull(message = "Nom du département ne peut pas être null")
    @NotEmpty(message = "Nom du département ne peut pas être vide")
    @Size(min = 5, max = 255, message = "Nom du département non valide, veuillez ressayer avec un autre")
    private String department;

    @Column(name = "year", updatable = true, insertable = true)
    @NotNull(message = "Annéene peut pas être null")
    @NotEmpty(message = "Annéene peut pas être vide")
    @Size(min = 5, max = 8, message = "Année non valide, veuillez ressayer avec un autre")
    private int startYear;

    


}
