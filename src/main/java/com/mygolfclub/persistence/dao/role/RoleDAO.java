package com.mygolfclub.persistence.dao.role;

import com.mygolfclub.entity.role.Role;

public interface RoleDAO {
    Role findByName(String roleName);
}
