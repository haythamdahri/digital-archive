package org.digital.archive.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student extends User {

    // department property
    @Column(name = "department", updatable = true, insertable = true)
    @NotNull(message = "Nom du département ne peut pas être null")
    @NotEmpty(message = "Nom du département ne peut pas être vide")
    @Size(min = 5, max = 255, message = "Nom du département non valide, veuillez ressayer avec un autre")
    private String department;

    @Column(name = "start_year", updatable = true, insertable = true)
    @NotNull(message = "Année ne peut pas être null")
    @NotEmpty(message = "Année ne peut pas être vide")
    @Size(min = 5, max = 8, message = "Année non valide, veuillez ressayer avec un autre")
    private int startYear;

    @Column(name = "finish_year", updatable = true, insertable = true)
    @NotNull(message = "Année ne peut pas être null")
    @NotEmpty(message = "Année ne peut pas être vide")
    @Size(min = 5, max = 8, message = "Année non valide, veuillez ressayer avec un autre")
    private int finishYear;

    @Enumerated(EnumType.STRING)
    private Level level;


}
