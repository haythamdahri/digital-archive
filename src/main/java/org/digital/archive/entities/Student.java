package org.digital.archive.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

/**
 * @author Haytham DAHRI
 */
@Entity
@Data
@DiscriminatorValue(value = "STUDENT")
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
    private Long startYear;

    @Column(name = "finish_year", updatable = true, insertable = true)
    private Long finishYear;

    // Prevent archive's delete when deleting a student
    @ManyToMany(mappedBy = "students", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Collection<Archive> archives;

    @Enumerated(EnumType.STRING)
    private Level level;

}
