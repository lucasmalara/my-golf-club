package com.mygolfclub.utils.mapping.role;

import com.mygolfclub.entity.role.Role;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;


@Component
public class RoleMapperImpl implements RoleMapper {

    @Override
    public SimpleGrantedAuthority mapTo(Role role) {
        return new SimpleGrantedAuthority(role.getName());
    }
}
