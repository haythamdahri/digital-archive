package org.digital.archive.services;

import org.digital.archive.entities.Archive;
import org.digital.archive.entities.Professor;
import org.digital.archive.entities.Student;
import org.digital.archive.repositories.ProfessorRepository;
import org.digital.archive.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        Collection<Student> students = new ArrayList<>();
        Iterable<Student> studentIterable = this.studentRepository.findAll();
        studentIterable.forEach(students::add);
        return students;
    }

    @Override
    public Page<Student> getStudents(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        return this.studentRepository.findAll(pageRequest);
    }

    @Override
    public Page<Student> getStudents(String search, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        return this.studentRepository.findByUsernameContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search, search, search, pageRequest);
    }


}
