package org.digital.archive.repositories;

import org.digital.archive.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;

/**
 * @author Haytham DAHRI
 */
@Repository
@RepositoryRestResource(path = "users")
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    User findByEmail(@Param("email") String email);

    User findByUsername(@Param("username") String username);

    Page<User> findByUsernameContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(@Param("username") String username, @Param("firstName") String firstName, @Param("lastName") String lastName, @Param("email") String email, @PageableDefault Pageable pageable);
}
