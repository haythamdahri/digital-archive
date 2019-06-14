package org.digital.archive.services;

import org.digital.archive.entities.Professor;
import org.digital.archive.entities.Student;
import org.digital.archive.repositories.ProfessorRepository;
import org.digital.archive.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Student saveStudent(Student student) {
        return this.studentRepository.save(student);
    }

    @Override
    public Student getStudent(Long id) {
        Optional<Student> studentOptional = this.studentRepository.findById(id);
        if( studentOptional.isPresent() ) {
            return studentOptional.get();
        }
        return null;
    }

    @Override
    public Student getStudent(String email) {
        return this.studentRepository.findByEmail(email);
    }

    @Override
    public boolean deleteStudent(Long id) {
        this.studentRepository.delete(this.getStudent(id));
        return true;
    }

    @Override
    public Collection<Student> getStudents() {
        return this.studentRepository.findAll();
    }


}
