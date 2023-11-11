package com.mygolfclub.service.role;

import com.mygolfclub.entity.role.Role;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

public interface RoleService {
    Role findByName(String role);
    Collection<String> getUserRoles(String role);
    Collection<SimpleGrantedAuthority> mapRolesToAuthorities(Collection<Role> roles);
}
