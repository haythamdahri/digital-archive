package org.digital.archive.services;


import org.digital.archive.entities.Student;
import org.springframework.data.domain.Page;

import java.util.Collection;

public interface StudentService {

    public Student saveStudent(Student student);

    public Student getStudent(Long id);

    public Student getStudent(String email);

    public boolean deleteStudent(Long id);

    public Collection<Student> getStudents();

    public Page<Student> getStudents(int page, int size);

    public Page<Student> getStudents(String search, int page, int size);

}
