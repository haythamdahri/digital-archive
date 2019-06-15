package org.digital.archive.services;


import org.digital.archive.entities.User;

import java.util.Collection;

public interface UserService {

    public User saveUser(User user);

    public User getUser(Long id);

    public User getUserByUsername(String username);

    public User getUser(String email);

    public boolean deleteUser(Long id);

    public Collection<User> getUsers();

}
