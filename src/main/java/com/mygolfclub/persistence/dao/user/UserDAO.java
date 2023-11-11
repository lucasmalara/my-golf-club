package com.mygolfclub.persistence.dao.user;

import com.mygolfclub.entity.user.User;

public interface UserDAO {
    User findByUsername(String username);
    void save(User user);
}
