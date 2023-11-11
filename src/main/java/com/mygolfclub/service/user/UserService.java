package com.mygolfclub.service.user;

import com.mygolfclub.entity.user.User;
import com.mygolfclub.model.user.UserModel;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findByUserName(String username);
    void save(UserModel userModel);
}
