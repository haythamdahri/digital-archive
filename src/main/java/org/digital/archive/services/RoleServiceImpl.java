package org.digital.archive.services;

import org.digital.archive.entities.Role;
import org.digital.archive.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role saveRole(Role role) {
        return this.roleRepository.save(role);
    }

    @Override
    public Role getRole(Long id) {
        Optional<Role> roleOptional = this.roleRepository.findById(id);
        if( roleOptional.isPresent() ) {
            return roleOptional.get();
        }
        return null;
    }

    @Override
    public boolean deleteRole(Long id) {
        this.roleRepository.delete(this.getRole(id));
        return true;
    }

    @Override
    public Collection<Role> getRoles() {
        return this.roleRepository.findAll();
    }
}
