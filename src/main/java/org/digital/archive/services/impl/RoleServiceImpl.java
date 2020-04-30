package org.digital.archive.services.impl;

import org.digital.archive.entities.Role;
import org.digital.archive.repositories.RoleRepository;
import org.digital.archive.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Haytham DAHRI
 */
@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role saveRole(Role role) {
        return this.roleRepository.save(role);
    }

    @Override
    public Role getRole(Long id) {
        Optional<Role> roleOptional = this.roleRepository.findById(id);
        return roleOptional.orElse(null);
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
