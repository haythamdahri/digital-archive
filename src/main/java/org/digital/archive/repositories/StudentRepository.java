package org.digital.archive.repositories;

import org.digital.archive.entities.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * @author Haytham DAHRI
 */
@Repository
@RepositoryRestResource(path = "students")
public interface StudentRepository extends PagingAndSortingRepository<Student, Long> {

    Student findByEmail(@Param("email") String email);

    Collection<Student> findByUsername(@Param("username") String username);

    Page<Student> findByUsernameContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(@Param("username") String username, @Param("firstName") String firstName, @Param("lastName") String lastName, @Param("email") String email, @PageableDefault Pageable pageable);

}
