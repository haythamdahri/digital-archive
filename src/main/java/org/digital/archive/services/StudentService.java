package org.digital.archive.services;


import org.digital.archive.entities.Student;

import java.util.Collection;

public interface StudentService {

    public Student saveStudent(Student student);

    public Student getStudent(Long id);

    public Student getStudent(String email);

    public boolean deleteStudent(Long id);

    public Collection<Student> getStudents();

}
