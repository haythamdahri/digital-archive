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

@Repository
@RepositoryRestResource(path = "/api/professors")
public interface ProfessorRepository extends PagingAndSortingRepository<Professor, Long> {

    public Professor findByEmail(@Param("email") String email);

    public Collection<Professor> findByUsername(@Param("username") String username);

    public Page<Professor> findByUsernameContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(@Param("username") String username, @Param("firstName") String firstName, @Param("lastName") String lastName, @Param("email") String email, @PageableDefault Pageable pageable);

}
