package org.digital.archive.services;

import org.digital.archive.entities.Student;
import org.digital.archive.entities.User;
import org.digital.archive.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User saveUser(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public User getUser(Long id) {
        Optional<User> optionalUser = this.userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    public User getUser(String email) {
        return this.userRepository.findByEmail(email);
    }


    @Override
    public boolean deleteUser(Long id) {
        this.userRepository.delete(this.getUser(id));
        return true;
    }

    @Override
    public Collection<User> getUsers() {
        Collection<User> users = new ArrayList<>();
        Iterable<User> userIterable = this.userRepository.findAll();
        userIterable.forEach(users::add);
        return users;
    }

    @Override
    public Page<User> getUsers(String search, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        return this.userRepository.findByUsernameContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search, search, search, pageRequest);
    }

}
