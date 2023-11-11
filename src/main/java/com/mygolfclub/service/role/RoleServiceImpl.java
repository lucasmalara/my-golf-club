package com.mygolfclub.service.role;

import com.mygolfclub.entity.role.Role;
import com.mygolfclub.persistence.dao.role.RoleDAO;
import com.mygolfclub.utils.roles.RoleTypes;
import com.mygolfclub.utils.mapping.role.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

import static com.mygolfclub.utils.roles.RoleTypes.*;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleDAO roleDAO;
    private final RoleMapper roleMapper;

    @Autowired
    public RoleServiceImpl(RoleDAO roleDAO, RoleMapper roleMapper) {
        this.roleDAO = roleDAO;
        this.roleMapper = roleMapper;
    }

    @Override
    public Role findByName(String role) {
        return roleDAO.findByName("ROLE_" + role);
    }

    @Override
    public Collection<String> getUserRoles(String role) {
        return switch (RoleTypes.valueOf(role)) {
            case EMPLOYEE -> List.of(EMPLOYEE.getValue());
            case MODERATOR -> List.of(
                    EMPLOYEE.getValue(),
                    MODERATOR.getValue()
            );
            case ADMIN -> List.of(
                    EMPLOYEE.getValue(),
                    MODERATOR.getValue(),
                    ADMIN.getValue()
            );
        };
    }

    @Override
    public Collection<SimpleGrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roleMapper.mapTo(roles);
    }
}
