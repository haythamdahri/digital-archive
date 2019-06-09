package org.digital.archive.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "archives")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Archive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "content")
    @NotNull(message = "Contenu ne peut pas être null")
    @NotEmpty(message = "Contenu ne peut pas être vide")
    @Size(min = 5, max = 800000, message = "Contenu non valide, veuillez ressayer avec un autre")
    private String content;

    @Column(name = "year")
    @NotNull(message = "Année ne peut pas être null")
    @NotEmpty(message = "Année ne peut pas être vide")
    @Size(min = 5, max = 8, message = "Année non valide, veuillez ressayer avec un autre")
    private Long year;

    @Column(name = "publish_date")
    @NotNull(message = "la date de publication ne peut pas être null")
    @NotEmpty(message = "la date de publication ne peut pas être vide")
    @Size(min = 5, max = 8, message = "la date de publication non valide, veuillez ressayer avec un autre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date publishDate;

    // Picture property
    @Column(name = "picture")
    @NotNull(message = "Image ne peut pas être null")
    @NotEmpty(message = "Image ne peut pas être vide")
    @Size(min = 5, max = 8500, message = "Image non valide, veuillez ressayer avec un autre")
    private String picture;

    // Prevent student's delete when deleting an archive
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE})
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToMany
    @JoinTable(name = "archives_professors",
    joinColumns = @JoinColumn(name = "archive_id"),
    inverseJoinColumns = @JoinColumn(name = "professor_id"))
    private Collection<Professor> professors;

    @Column(name = "file")
    @NotNull(message = "Archive ne peut pas être null")
    @NotEmpty(message = "Archive ne peut pas être vide")
    @Size(min = 5, max = 8000, message = "Archive non valide, veuillez ressayer avec un autre")
    private String file;


}
