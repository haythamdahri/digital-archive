package org.digital.archive.repositories;

import org.digital.archive.entities.Student;
import org.digital.archive.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;
import java.util.Collection;

@Repository
@RepositoryRestResource(path = "/api/students")
public interface StudentRepository extends PagingAndSortingRepository<Student, Long> {

    public Student findByEmail(@Param("email") String email);

    public Collection<Student> findByUsername(@Param("username") String username);

    public Page<Student> findByUsernameContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(@Param("username") String username, @Param("firstName") String firstName, @Param("lastName") String lastName, @Param("email") String email, @PageableDefault Pageable pageable);

}
