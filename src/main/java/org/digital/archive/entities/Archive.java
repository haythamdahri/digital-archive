package org.digital.archive.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * @author Haytham DAHRI
 */
@Entity
@Table(name = "archives")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Archive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "views")
    private Long views;

    @Column(name = "title")
    @NotNull(message = "Titre de l'article ne peut pas être null")
    @NotEmpty(message = "Titre de l'article ne peut pas être vide")
    @Size(min = 5, max = 250, message = "Titre de l'article non valide, veuillez ressayer avec un autre")
    private String title;

    @Column(name = "content")
    @NotNull(message = "Contenu ne peut pas être null")
    @NotEmpty(message = "Contenu ne peut pas être vide")
    @Size(min = 5, max = 800000, message = "Contenu non valide, veuillez ressayer avec un autre")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "professor_id")
    private Professor publisher;

    @Column(name = "publish_date")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishDate;

    // Picture property
    @Column(name = "image")
    @NotNull(message = "Image ne peut pas être null")
    @NotEmpty(message = "Image ne peut pas être vide")
    @Size(min = 5, max = 8500, message = "Image non valide, veuillez ressayer avec un autre")
    private String image;

    // students's archive
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "archives_students", joinColumns = @JoinColumn(name = "archive_id"), inverseJoinColumns = @JoinColumn(name = "student_id"))
    private Collection<Student> students;

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

    /*
     * Add a student to the archive
     */
    public void addStudent(Student student) {
        if (this.students == null) {
            this.students = new ArrayList<>();
        }
        this.students.add(student);
    }

    /*
     * Add a professor to the archive
     */
    public void addProfessor(Professor professor) {
        if (this.professors == null) {
            this.professors = new ArrayList<>();
        }
        this.professors.add(professor);
    }


}
