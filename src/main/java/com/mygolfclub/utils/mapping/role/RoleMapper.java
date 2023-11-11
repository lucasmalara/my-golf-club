package com.mygolfclub.utils.mapping.role;

import com.mygolfclub.entity.role.Role;
import com.mygolfclub.utils.mapping.OneWayMappable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public interface RoleMapper extends OneWayMappable<Role, SimpleGrantedAuthority> {

}
