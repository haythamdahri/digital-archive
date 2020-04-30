package org.digital.archive.services;

import org.digital.archive.entities.Student;
import org.springframework.data.domain.Page;

import java.util.Collection;

/**
 * @author Haytham DAHRI
 */
public interface StudentService {

    Student saveStudent(Student student);

    Student getStudent(Long id);

    Student getStudent(String email);

    boolean deleteStudent(Long id);

    Collection<Student> getStudents();

    Page<Student> getStudents(int page, int size);

    Page<Student> getStudents(String search, int page, int size);

}
