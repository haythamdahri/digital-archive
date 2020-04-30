package org.digital.archive.services;

import org.digital.archive.entities.Role;

import java.util.Collection;

/**
 * @author Haytham DAHRI
 */
public interface RoleService {

    Role saveRole(Role role);

    Role getRole(Long id);

    boolean deleteRole(Long id);

    Collection<Role> getRoles();

}
