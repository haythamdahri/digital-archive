package org.digital.archive.repositories;

import org.digital.archive.entities.Professor;
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
@RepositoryRestResource(path = "professors")
public interface ProfessorRepository extends PagingAndSortingRepository<Professor, Long> {

    Professor findByEmail(@Param("email") String email);

    Collection<Professor> findByUsername(@Param("username") String username);

    Page<Professor> findByUsernameContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(@Param("username") String username, @Param("firstName") String firstName, @Param("lastName") String lastName, @Param("email") String email, @PageableDefault Pageable pageable);

}
