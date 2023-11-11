package com.mygolfclub.persistence.dao.role;

import com.mygolfclub.entity.role.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDAOImpl implements RoleDAO {

    private final EntityManager entityManager;

    @Autowired
    public RoleDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Role findByName(String roleName) {
        TypedQuery<Role> query = entityManager.createQuery("from Role where name=:role", Role.class);
        query.setParameter("role", roleName);
        Role role;
        try {
            role = query.getSingleResult();
        } catch (Exception e) {
            role = new Role();
        }
        return role;
    }
}
