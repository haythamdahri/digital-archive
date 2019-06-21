package org.digital.archive.repositories;

import org.digital.archive.entities.Professor;
import org.digital.archive.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
@RepositoryRestResource(path = "/api/professors")
public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    public Professor findByEmail(@Param("email") String email);

    public Collection<Professor> findByUsername(@Param("username") String username);

}
