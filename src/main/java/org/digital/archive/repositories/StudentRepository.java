package org.digital.archive.repositories;

import org.digital.archive.entities.Student;
import org.digital.archive.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
@RepositoryRestResource(path = "/api/students")
public interface StudentRepository extends JpaRepository<Student, Long> {

    public Student findByEmail(@Param("email") String email);

    public Collection<User> findByUsername(@Param("username") String username);

}
