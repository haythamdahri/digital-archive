package org.digital.archive.services;

import org.digital.archive.entities.User;
import org.springframework.data.domain.Page;

import java.util.Collection;

/**
 * @author Haytham DAHRI
 */
public interface UserService {

    User saveUser(User user);

    User getUser(Long id);

    User getUserByUsername(String username);

    User getUser(String email);

    boolean deleteUser(Long id);

    Collection<User> getUsers();

    Page<User> getUsers(String search, int page, int size);

}
