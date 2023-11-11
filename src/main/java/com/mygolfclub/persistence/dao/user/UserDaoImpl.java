package com.mygolfclub.persistence.dao.user;

import com.mygolfclub.entity.user.User;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDAO{

    private final EntityManager entityManager;

    @Autowired
    public UserDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public @Nullable User findByUsername(String username) {
        TypedQuery<User> query = entityManager.createQuery("from User where username=:theUsername", User.class);
        query.setParameter("theUsername", username);
        User user;
        try {
            user = query.getSingleResult();
        } catch (Exception e) {
            user = null;
        }
        return user;
    }

    @Override
    @Transactional
    public void save(User user) {
        entityManager.merge(user);
    }
}
