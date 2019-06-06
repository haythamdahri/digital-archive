package org.digital.archive.services;


import org.digital.archive.entities.Role;

import java.util.Collection;

public interface RoleService {

    public Role saveRole(Role role);

    public Role getRole(Long id);

    public boolean deleteRole(Long id);

    public Collection<Role> getRoles();

}
