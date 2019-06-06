package org.digital.archive.repositories;

import org.digital.archive.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource
public interface RoleRepository extends JpaRepository<Role, Long> {

}
